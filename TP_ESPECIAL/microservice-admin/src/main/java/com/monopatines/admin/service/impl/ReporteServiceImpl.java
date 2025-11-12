package com.monopatines.admin.service.impl;
import com.monopatines.admin.client.FacturacionClient; 
import com.monopatines.admin.client.MonopatinClient; 
import com.monopatines.admin.client.UserClient;
import com.monopatines.admin.client.ViajesClient;
import com.monopatines.admin.dto.*; 
import com.monopatines.admin.service.ReporteService; 
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate; 
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service 
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReporteServiceImpl implements ReporteService {
    private final MonopatinClient monopatinClient; 
    private final FacturacionClient facturacionClient; 
    private final UserClient userClient;
    private final ViajesClient viajesClient;

    @Override
    public List<UsoMonopatinDTO> reporteUso(LocalDate desde, LocalDate hasta, boolean incluirPausas){ 
        return monopatinClient.reporteUso(desde, hasta, incluirPausas); 
    }

    @Override
    public List<MonopatinResumenDTO> monopatinesConMasViajes(int anio, int minViajes){ 
        return monopatinClient.monopatinesConMasViajes(anio, minViajes); 
    }
 
    @Override
    public TotalFacturadoDTO totalFacturado(int anio, int mesDesde, int mesHasta){ 
        return facturacionClient.total(anio, mesDesde, mesHasta); 
    }
 
    @Override
    public List<UsuarioUsoDTO> usuariosMasFrecuentes(LocalDate desde, LocalDate hasta, String tipoCuenta){ 
        log.info("Generando reporte de usuarios más frecuentes desde {} hasta {}, tipo: {}", 
                desde, hasta, tipoCuenta);
        
        LocalDateTime inicioDateTime = desde.atStartOfDay();
        LocalDateTime finDateTime = hasta.atTime(23, 59, 59);
        
        // Obtener todos los usuarios
        PageDTO<UsuarioDTO> pageUsuarios = userClient.getAllUsuarios();
        List<UsuarioDTO> todosUsuarios = pageUsuarios.getContent();
        
        // Filtrar por tipo de cuenta si se especifica
        if (tipoCuenta != null && !tipoCuenta.isBlank()) {
            todosUsuarios = todosUsuarios.stream()
                    .filter(u -> u.getCuentas() != null && u.getCuentas().stream()
                            .anyMatch(c -> c.getTipoCuenta().equalsIgnoreCase(tipoCuenta)))
                    .collect(Collectors.toList());
        }
        
        // Calcular estadísticas para cada usuario
        List<UsuarioUsoDTO> resultado = new ArrayList<>();
        
        for (UsuarioDTO usuario : todosUsuarios) {
            try {
                List<ViajeDTO> viajes = viajesClient.getViajesPorUsuarioEnPeriodo(
                        usuario.getId(), 
                        inicioDateTime, 
                        finDateTime
                );
                
                if (viajes == null || viajes.isEmpty()) {
                    continue; // Saltar usuarios sin viajes
                }
                
                int cantidadViajes = viajes.size();
                double totalKm = viajes.stream()
                        .mapToDouble(v -> v.getDistanciaRecorrida() != null ? v.getDistanciaRecorrida() : 0.0)
                        .sum();
                
                long totalMinutos = viajes.stream()
                        .filter(v -> v.getHoraInicio() != null && v.getHoraFin() != null)
                        .mapToLong(v -> Duration.between(v.getHoraInicio(), v.getHoraFin()).toMinutes())
                        .sum();
                
                String tipoCuentaUsuario = usuario.getCuentas() != null && !usuario.getCuentas().isEmpty()
                        ? usuario.getCuentas().get(0).getTipoCuenta()
                        : "SIN_CUENTA";
                
                UsuarioUsoDTO dto = new UsuarioUsoDTO(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getEmail(),
                        tipoCuentaUsuario,
                        cantidadViajes,
                        totalKm,
                        totalMinutos
                );
                
                resultado.add(dto);
                
            } catch (Exception e) {
                log.error("Error al procesar viajes del usuario {}: {}", usuario.getId(), e.getMessage());
            }
        }
        
        // Ordenar por cantidad de viajes (descendente)
        resultado.sort((a, b) -> Integer.compare(b.getCantidadViajes(), a.getCantidadViajes()));
        
        log.info("Reporte generado con {} usuarios", resultado.size());
        return resultado;
    }
}