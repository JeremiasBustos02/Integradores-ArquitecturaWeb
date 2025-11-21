package com.microservices.microserviceiaservice.service;

import com.microservices.microserviceiaservice.client.GroqChatClient;
import com.microservices.microserviceiaservice.dto.RespuestaApi;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.bson.Document;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IAService {
    @Autowired
    private MongoTemplate mongoTemplate;

    public IAService() {
        this.CONTEXTO_SQL = cargarArchivo("esquema_completo.sql");

        this.CONTEXTO_MONGO = cargarArchivo("esquema_mongo.txt");

    }

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private GroqChatClient groqChatClient;

    private final String CONTEXTO_SQL;
    private final String CONTEXTO_MONGO;


    private static final Logger log = LoggerFactory.getLogger(IAService.class);
    //aceptar solo sentencias que empiecen con SELECT..
    private static final Pattern SQL_ALLOWED =
            Pattern.compile("(?is)\\b(SELECT|INSERT|UPDATE|DELETE)\\b[\\s\\S]*?;");

    // Bloqueamos DDL u otras operaciones peligrosas
    private static final Pattern SQL_FORBIDDEN =
            Pattern.compile("(?i)\\b(DROP|TRUNCATE|ALTER|CREATE|GRANT|REVOKE)\\b");

    private String cargarArchivo(String nombreArchivo) {
        try (InputStream inputStream = new ClassPathResource(nombreArchivo).getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("No se pudo cargar el archivo de contexto: " + nombreArchivo);
            return ""; // Devuelve vacío para no romper la app si falta el archivo
        }
    }

    // Genera el prompt, obtiene la SQL de Groq, la valida y ejecuta, y diferencia
    //mongo de sql
    @Transactional
    public ResponseEntity<?> procesarPrompt(String promptUsuario) {
        try {
            // Construimos un prompt HÍBRIDO
            String promptFinal = """
                    Actúa como un experto en bases de datos y sistemas. Tengo dos fuentes de datos:
                    
                    1. BASE DE DATOS SQL (MySQL) - Tablas y relaciones:
                    %s
                    
                    2. BASE DE DATOS NoSQL (MongoDB) - Colecciones y documentos:
                    %s
                    
                    TU TAREA: Analiza la pregunta del usuario: "%s" y decide cuál base de datos consultar.
                    
                    REGLAS DE RESPUESTA (Estrictas):
                    - Si la respuesta requiere SQL (MySQL), devuelve ÚNICAMENTE la sentencia SQL válida terminada en ';'.
                    - Si la respuesta requiere MongoDB, devuelve ÚNICAMENTE un objeto JSON válido para ejecutar un comando (command style).
                      Ejemplo Mongo: { "find": "viajes", "filter": { "anio": 2023 } } or { "aggregate": "viajes", "pipeline": [...] }
                      NO uses sintaxis 'db.collection.find()', usa solo JSON estricto compatible con 'runCommand'.
                    
                    - NO agregues explicaciones, ni markdown, ni saludos. SOLO EL CÓDIGO.
                    """.formatted(CONTEXTO_SQL, CONTEXTO_MONGO, promptUsuario);

            log.info("==== PROMPT ENVIADO ====\n{}", promptFinal);

            String respuestaIa = groqChatClient.preguntar(promptFinal).trim();
            log.info("==== RESPUESTA IA ====\n{}", respuestaIa);


            // Si empieza con '{', asumimos que es JSON para Mongo
            if (respuestaIa.startsWith("{") || respuestaIa.startsWith("```json")) {
                return ejecutarConsultaMongo(respuestaIa);
            } else {
                // Si no, asumimos que es SQL
                return ejecutarConsultaSQL(respuestaIa);
            }

        } catch (Exception e) {
            log.error("Fallo al procesar prompt", e);
            return new ResponseEntity<>(
                    new RespuestaApi<>(false, "Error al procesar el prompt: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    private ResponseEntity<?> ejecutarConsultaMongo(String respuestaIa) {
        try {
            // Limpiamos posible markdown (```json ... ```)
            String jsonCommand = extraerJsonMongo(respuestaIa);

            if (jsonCommand == null || jsonCommand.isEmpty()) {
                return ResponseEntity.badRequest().body(new RespuestaApi<>(false, "La IA no generó un JSON válido para Mongo.", null));
            }

            log.info("==== EJECUTANDO COMANDO MONGO ====\n{}", jsonCommand);

            // Convertimos el String JSON a un Documento BSON
            Document command = Document.parse(jsonCommand);

            Document result = mongoTemplate.executeCommand(command);

            // Limpieza del resultado: Mongo devuelve muchos metadatos.
            Object data;
            if (result.containsKey("cursor")) {
                data = ((Document) result.get("cursor")).get("firstBatch");
            } else {
                data = result;
            }

            return ResponseEntity.ok(new RespuestaApi<>(true, "Consulta MongoDB ejecutada con éxito", data));

        } catch (Exception e) {
            log.warn("Error ejecutando Mongo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RespuestaApi<>(false, "Error en consulta Mongo: " + e.getMessage(), null));
        }
    }

    private ResponseEntity<?> ejecutarConsultaSQL(String respuestaIa) {
        String sql = extraerConsultaSQL(respuestaIa);
        if (sql == null || sql.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RespuestaApi<>(false, "No se encontró una sentencia SQL válida.", null));
        }

        log.info("==== EJECUTANDO SQL ====\n{}", sql);

        // Quitamos el punto y coma final para JPA/JDBC
        String sqlToExecute = sql.endsWith(";") ? sql.substring(0, sql.length() - 1) : sql;

        try {
            Object data;
            // SELECT
            if (sql.trim().regionMatches(true, 0, "SELECT", 0, 6)) {
                @SuppressWarnings("unchecked")
                List<Object[]> resultados = entityManager.createNativeQuery(sqlToExecute).getResultList();
                data = resultados;
                return ResponseEntity.ok(new RespuestaApi<>(true, "Consulta SQL ejecutada con éxito", data));
            } else {
                // INSERT, UPDATE, DELETE
                int rows = entityManager.createNativeQuery(sqlToExecute).executeUpdate();
                data = rows;
                return ResponseEntity.ok(new RespuestaApi<>(true, "Sentencia DML ejecutada con éxito. Filas afectadas: " + rows, data));
            }
        } catch (Exception e) {
            log.warn("Error SQL: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RespuestaApi<>(false, "Error SQL: " + e.getMessage(), null));
        }
    }


    private String extraerConsultaSQL(String respuesta) {
        if (respuesta == null) return null;
        Matcher m = SQL_ALLOWED.matcher(respuesta);
        if (!m.find()) return null;
        String sql = m.group().trim();
        int first = sql.indexOf(';');
        if (first > -1) sql = sql.substring(0, first + 1);
        if (SQL_FORBIDDEN.matcher(sql).find()) {
            log.warn("SQL Bloqueado: {}", sql);
            return null;
        }
        return sql;
    }

    private String extraerJsonMongo(String respuesta) {
        if (respuesta == null) return null;
        // Si la IA responde con bloques de código ```json ... ```
        if (respuesta.contains("```")) {
            respuesta = respuesta.replaceAll("```json", "").replaceAll("```", "");
        }
        return respuesta.trim();
    }
}
