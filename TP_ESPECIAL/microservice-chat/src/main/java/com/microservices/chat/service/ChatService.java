package com.microservices.chat.service;

import com.microservices.chat.client.GroqClient;
import com.microservices.chat.dto.ChatResponseDTO;
import com.microservices.chat.entity.Usuario;
import com.microservices.chat.exception.UnauthorizedException;
import com.microservices.chat.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private GroqClient groqClient;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final String CONTEXTO_SQL;

    private static final Pattern SQL_ALLOWED =
            Pattern.compile("(?is)\\b(SELECT)\\b[\\s\\S]*?;");

    private static final Pattern SQL_FORBIDDEN =
            Pattern.compile("(?i)\\b(DROP|TRUNCATE|ALTER|CREATE|GRANT|REVOKE|DELETE|UPDATE|INSERT)\\b");

    public ChatService() {
        this.CONTEXTO_SQL = cargarEsquemaSQL("esquema_completo.sql");
    }

    private String cargarEsquemaSQL(String nombreArchivo) {
        try (InputStream inputStream = new ClassPathResource(nombreArchivo).getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error al leer el archivo SQL desde resources: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public ChatResponseDTO procesarMensaje(Long usuarioId, String mensaje) {
        try {
            if (!esUsuarioPremium(usuarioId)) {
                throw new UnauthorizedException("Solo los usuarios premium pueden usar el servicio de chat con IA");
            }

            log.info("Usuario {} (premium) realizó consulta: {}", usuarioId, mensaje);

            String promptFinal = """
                    Este es el esquema de mi base de datos MySQL del sistema de monopatines:
                    %s

                    Basándote exclusivamente en este esquema, devolveme ÚNICAMENTE una sentencia SQL
                    MySQL completa y VÁLIDA (sin texto adicional, sin markdown, sin comentarios) que
                    termine con punto y coma. La sentencia debe ser SELECT solamente (consulta de lectura).
                    
                    Pregunta del usuario: %s
                    """.formatted(CONTEXTO_SQL, mensaje);

            log.info("==== PROMPT ENVIADO A LA IA ====\n{}", promptFinal);

            String respuestaIa = groqClient.preguntar(promptFinal);
            log.info("==== RESPUESTA IA ====\n{}", respuestaIa);

            String sql = extraerConsultaSQL(respuestaIa);
            if (sql == null || sql.isEmpty()) {
                return new ChatResponseDTO(false, 
                    "No pude generar una consulta válida para tu pregunta. ¿Podrías reformularla?", 
                    null);
            }

            log.info("==== SQL EXTRAÍDA ====\n{}", sql);

            String sqlToExecute = sql.endsWith(";") ? sql.substring(0, sql.length() - 1) : sql;

            try {
                @SuppressWarnings("unchecked")
                List<Object> resultados = entityManager.createNativeQuery(sqlToExecute).getResultList();
                
                String respuestaFormateada = formatearResultados(resultados, mensaje);
                
                return new ChatResponseDTO(true, respuestaFormateada, resultados);

            } catch (Exception e) {
                log.warn("Error al ejecutar SQL: {}", e.getMessage(), e);
                return new ChatResponseDTO(false, 
                    "Error al ejecutar la consulta. Por favor, intenta reformular tu pregunta.", 
                    null);
            }

        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            log.error("Fallo al procesar mensaje del chat", e);
            return new ChatResponseDTO(false, 
                "Error al procesar tu consulta: " + e.getMessage(), 
                null);
        }
    }

    private boolean esUsuarioPremium(Long usuarioId) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
            if (usuarioOpt.isEmpty()) {
                return false;
            }
            
            Usuario usuario = usuarioOpt.get();
            String rol = usuario.getRol().toUpperCase();
            return "PREMIUM".equals(rol);
            
        } catch (Exception e) {
            log.error("Error al verificar si usuario {} es premium: {}", usuarioId, e.getMessage());
            return false;
        }
    }

    private String extraerConsultaSQL(String respuesta) {
        if (respuesta == null) return null;

        Matcher m = SQL_ALLOWED.matcher(respuesta);
        if (!m.find()) return null;

        String sql = m.group().trim();

        int first = sql.indexOf(';');
        if (first > -1) {
            sql = sql.substring(0, first + 1);
        }

        if (SQL_FORBIDDEN.matcher(sql).find()) {
            log.warn("Sentencia bloqueada por contener operaciones prohibidas: {}", sql);
            return null;
        }

        return sql;
    }

    private String formatearResultados(List<Object> resultados, String preguntaOriginal) {
        if (resultados == null || resultados.isEmpty()) {
            return "No encontré resultados para tu consulta.";
        }

        boolean esEscalar = !(resultados.get(0) instanceof Object[]);
        
        if (esEscalar) {
            if (resultados.size() == 1) {
                Object valor = resultados.get(0);
                
                String preguntaLower = preguntaOriginal.toLowerCase();
                if (preguntaLower.contains("cuántos") || preguntaLower.contains("cuántas") || 
                    preguntaLower.contains("cantidad") || preguntaLower.contains("total")) {
                    
                    if (preguntaLower.contains("parada")) {
                        return String.format("Hay **%s paradas** disponibles en el sistema.", valor);
                    } else if (preguntaLower.contains("viaje")) {
                        return String.format("Hay **%s viajes** registrados en el sistema.", valor);
                    } else if (preguntaLower.contains("usuario")) {
                        return String.format("Hay **%s usuarios** registrados en el sistema.", valor);
                    } else {
                        return String.format("El resultado es: **%s**", valor);
                    }
                }
                return String.format("Resultado: **%s**", valor);
            }
            
            StringBuilder sb = new StringBuilder("Resultados:\n\n");
            for (int i = 0; i < resultados.size(); i++) {
                sb.append((i + 1)).append(". ").append(resultados.get(i)).append("\n");
            }
            return sb.toString();
        }

        StringBuilder sb = new StringBuilder();
        
        if (preguntaOriginal.toLowerCase().contains("parada")) {
            sb.append("Encontré ").append(resultados.size()).append(" parada(s):\n\n");
            for (int i = 0; i < resultados.size(); i++) {
                Object[] row = (Object[]) resultados.get(i);
                sb.append(i + 1).append(". ");
                if (row.length >= 3) {
                    sb.append("**").append(row[1]).append("** - ");
                    sb.append(row[2]);
                    if (row.length >= 6) {
                        sb.append(" (Capacidad: ").append(row[5]).append(" monopatines)");
                    }
                }
                sb.append("\n");
            }
        } else if (preguntaOriginal.toLowerCase().contains("viaje")) {
            sb.append("Encontré ").append(resultados.size()).append(" viaje(s):\n\n");
            for (int i = 0; i < Math.min(resultados.size(), 10); i++) {
                Object[] row = (Object[]) resultados.get(i);
                sb.append(i + 1).append(". ");
                if (row.length >= 5) {
                    sb.append("Fecha: ").append(row[1]);
                    if (row[3] != null) {
                        sb.append(" - ").append(row[3]).append(" km");
                    }
                    if (row[4] != null) {
                        sb.append(" - ").append(row[4]).append(" min");
                    }
                    if (row[5] != null) {
                        sb.append(" - $").append(row[5]);
                    }
                }
                sb.append("\n");
            }
            if (resultados.size() > 10) {
                sb.append("\n... y ").append(resultados.size() - 10).append(" viaje(s) más.");
            }
        } else {
            sb.append("Encontré ").append(resultados.size()).append(" resultado(s):\n\n");
            for (int i = 0; i < Math.min(resultados.size(), 15); i++) {
                Object[] row = (Object[]) resultados.get(i);
                sb.append(i + 1).append(". ");
                for (int j = 0; j < row.length; j++) {
                    sb.append(row[j]);
                    if (j < row.length - 1) sb.append(" | ");
                }
                sb.append("\n");
            }
            if (resultados.size() > 15) {
                sb.append("\n... y ").append(resultados.size() - 15).append(" resultado(s) más.");
            }
        }

        return sb.toString();
    }
}
