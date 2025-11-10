package com.microservices.microservicemonopatin.service.impl;

import com.microservices.microservicemonopatin.dto.MonopatinCercanoDTO;
import com.microservices.microservicemonopatin.dto.MonopatinDTO;
import com.microservices.microservicemonopatin.dto.MonopatinRequestDTO;
import com.microservices.microservicemonopatin.dto.ReporteKilometrosDTO;
import com.microservices.microservicemonopatin.entity.EstadoMonopatin;
import com.microservices.microservicemonopatin.entity.Monopatin;
import com.microservices.microservicemonopatin.exception.MonopatinNotFoundException;
import com.microservices.microservicemonopatin.feignClient.ViajeFeignClient;
import com.microservices.microservicemonopatin.mapper.MonopatinMapper;
import com.microservices.microservicemonopatin.repository.MonopatinRepository;
import com.microservices.microservicemonopatin.service.MonopatinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MonopatinServiceImpl implements MonopatinService {

    private final MonopatinRepository monopatinRepository;
    private final MonopatinMapper monopatinMapper;
    private final ViajeFeignClient viajeFeignClient; // Para consultar cantidad de viajes

    @Override
    public MonopatinDTO crear(MonopatinRequestDTO dto) {
        log.info("Creando monopatín con ubicación: ({}, {})", dto.getLatitud(), dto.getLongitud());

        Monopatin monopatin = monopatinMapper.toEntity(dto);
        Monopatin savedMonopatin = monopatinRepository.save(monopatin);

        log.info("Monopatín creado con ID: {}", savedMonopatin.getId());
        return monopatinMapper.toDTO(savedMonopatin);
    }

    @Override
    @Transactional(readOnly = true)
    public MonopatinDTO obtenerPorId(Long id) {
        log.info("Buscando monopatín con ID: {}", id);

        Monopatin monopatin = monopatinRepository.findById(id)
                .orElseThrow(() -> new MonopatinNotFoundException("Monopatín no encontrado con ID: " + id));

        return monopatinMapper.toDTO(monopatin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonopatinDTO> obtenerTodos() {
        log.info("Obteniendo todos los monopatines");

        List<Monopatin> monopatines = monopatinRepository.findAll();
        return monopatinMapper.toDTOList(monopatines);
    }

    @Override
    public MonopatinDTO actualizar(Long id, MonopatinRequestDTO dto) {
        log.info("Actualizando monopatín con ID: {}", id);

        Monopatin monopatin = monopatinRepository.findById(id)
                .orElseThrow(() -> new MonopatinNotFoundException("Monopatín no encontrado con ID: " + id));

        // Actualizar solo campos permitidos
        monopatin.setLatitud(dto.getLatitud());
        monopatin.setLongitud(dto.getLongitud());
        monopatin.setEstado(dto.getEstado());

        if (dto.getIdParadaActual() != null) {
            monopatin.setIdParadaActual(dto.getIdParadaActual());
        }

        Monopatin updatedMonopatin = monopatinRepository.save(monopatin);
        log.info("Monopatín actualizado con ID: {}", id);

        return monopatinMapper.toDTO(updatedMonopatin);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando monopatín con ID: {}", id);

        if (!monopatinRepository.existsById(id)) {
            throw new MonopatinNotFoundException("Monopatín no encontrado con ID: " + id);
        }

        monopatinRepository.deleteById(id);
        log.info("Monopatín eliminado con ID: {}", id);
    }

    @Override
    public MonopatinDTO marcarEnMantenimiento(Long id) {
        log.info("Marcando monopatín {} en mantenimiento", id);

        Monopatin monopatin = monopatinRepository.findById(id)
                .orElseThrow(() -> new MonopatinNotFoundException("Monopatín no encontrado con ID: " + id));

        monopatin.setEnMantenimiento(true);
        monopatin.setEstado(EstadoMonopatin.MANTENIMIENTO);

        Monopatin updated = monopatinRepository.save(monopatin);
        log.info("Monopatín {} marcado en mantenimiento", id);

        return monopatinMapper.toDTO(updated);
    }

    @Override
    public MonopatinDTO sacarDeMantenimiento(Long id) {
        log.info("Sacando monopatín {} de mantenimiento", id);

        Monopatin monopatin = monopatinRepository.findById(id)
                .orElseThrow(() -> new MonopatinNotFoundException("Monopatín no encontrado con ID: " + id));

        monopatin.setEnMantenimiento(false);
        monopatin.setEstado(EstadoMonopatin.DISPONIBLE);

        Monopatin updated = monopatinRepository.save(monopatin);
        log.info("Monopatín {} sacado de mantenimiento", id);

        return monopatinMapper.toDTO(updated);
    }

    @Override
    public MonopatinDTO cambiarEstado(Long id, EstadoMonopatin nuevoEstado) {
        log.info("Cambiando estado de monopatín {} a {}", id, nuevoEstado);

        Monopatin monopatin = monopatinRepository.findById(id)
                .orElseThrow(() -> new MonopatinNotFoundException("Monopatín no encontrado con ID: " + id));

        monopatin.setEstado(nuevoEstado);
        Monopatin updated = monopatinRepository.save(monopatin);

        return monopatinMapper.toDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonopatinCercanoDTO> buscarCercanos(Double latitud, Double longitud, Double radioKm) {
        log.info("Buscando monopatines cercanos a ({}, {}) en radio de {} km", latitud, longitud, radioKm);

        List<Monopatin> disponibles = monopatinRepository.findDisponibles();
        List<MonopatinCercanoDTO> cercanos = new ArrayList<>();

        for (Monopatin m : disponibles) {
            double distancia = calcularDistancia(latitud, longitud, m.getLatitud(), m.getLongitud());

            if (distancia <= radioKm) {
                MonopatinCercanoDTO dto = monopatinMapper.toCercanoDTO(m);
                dto.setDistancia(Math.round(distancia * 100.0) / 100.0); // Redondear a 2 decimales
                cercanos.add(dto);
            }
        }

        // Ordenar por distancia
        cercanos.sort((a, b) -> Double.compare(a.getDistancia(), b.getDistancia()));

        log.info("Encontrados {} monopatines cercanos", cercanos.size());
        return cercanos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteKilometrosDTO> reporteKilometros(boolean incluirPausas) {
        log.info("Generando reporte de kilómetros (incluir pausas: {})", incluirPausas);

        List<Monopatin> monopatines = monopatinRepository.findAllOrderByKmTotales();
        List<ReporteKilometrosDTO> reporte = new ArrayList<>();

        for (Monopatin m : monopatines) {
            ReporteKilometrosDTO dto = new ReporteKilometrosDTO();
            dto.setId(m.getId());
            dto.setKmTotales(m.getKmTotales());
            dto.setTiempoUsoTotal(m.getTiempoUsoTotal());
            dto.setTiempoPausasTotal(m.getTiempoPausasTotales());

            if (incluirPausas) {
                dto.setTiempoUsoSinPausas(m.getTiempoUsoTotal() - m.getTiempoPausasTotales());
            } else {
                dto.setTiempoUsoSinPausas(m.getTiempoUsoTotal());
            }

            reporte.add(dto);
        }

        return reporte;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonopatinDTO> obtenerConMasDeXViajes(int cantidadMinima) {
        log.info("Buscando monopatines con más de {} viajes", cantidadMinima);

        // Obtener todos los monopatines
        List<Monopatin> todos = monopatinRepository.findAll();
        List<MonopatinDTO> resultado = new ArrayList<>();

        for (Monopatin m : todos) {
            // Consultar al microservicio de viajes cuántos viajes tiene este monopatín
            try {
                Integer cantidadViajes = viajeFeignClient.contarViajesPorMonopatin(m.getId());

                if (cantidadViajes != null && cantidadViajes >= cantidadMinima) {
                    resultado.add(monopatinMapper.toDTO(m));
                }
            } catch (Exception e) {
                log.error("Error al consultar viajes del monopatín {}: {}", m.getId(), e.getMessage());
            }
        }

        log.info("Encontrados {} monopatines con más de {} viajes", resultado.size(), cantidadMinima);
        return resultado;
    }

    @Override
    public void actualizarUbicacion(Long id, Double latitud, Double longitud) {
        log.info("Actualizando ubicación del monopatín {} a ({}, {})", id, latitud, longitud);

        Monopatin monopatin = monopatinRepository.findById(id)
                .orElseThrow(() -> new MonopatinNotFoundException("Monopatín no encontrado con ID: " + id));

        monopatin.setLatitud(latitud);
        monopatin.setLongitud(longitud);

        monopatinRepository.save(monopatin);
        log.info("Ubicación actualizada para monopatín {}", id);
    }

    @Override
    public void registrarUsoDeViaje(Long id, Double kmRecorridos, Long tiempoUso, Long tiempoPausas) {
        log.info("Registrando uso del monopatín {}: {} km, {} min uso, {} min pausas",
                id, kmRecorridos, tiempoUso, tiempoPausas);

        Monopatin monopatin = monopatinRepository.findById(id)
                .orElseThrow(() -> new MonopatinNotFoundException("Monopatín no encontrado con ID: " + id));

        monopatin.setKmTotales(monopatin.getKmTotales() + kmRecorridos);
        monopatin.setTiempoUsoTotal(monopatin.getTiempoUsoTotal() + tiempoUso);
        monopatin.setTiempoPausasTotales(monopatin.getTiempoPausasTotales() + tiempoPausas);

        monopatinRepository.save(monopatin);
        log.info("Uso registrado para monopatín {}", id);
    }

    /**
     * Calcula la distancia entre dos puntos usando la fórmula de Haversine
     * @return distancia en kilómetros
     */
    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final int RADIO_TIERRA_KM = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RADIO_TIERRA_KM * c;
    }
}