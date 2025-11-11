package com.microservices.viajes.service;

import com.microservices.viajes.dto.request.FinalizarViajeRequestDTO;
import com.microservices.viajes.dto.request.ViajeRequestDTO;
import com.microservices.viajes.dto.response.ReporteUsoMonopatinDTO;
import com.microservices.viajes.dto.response.ViajeResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ViajeServiceI {
    ViajeResponseDTO createViaje(Long monopatinId, Long usuarioId, Long tarifaId, Long paradaInicioId);

    ViajeResponseDTO finalizarViaje(String id, Long idParada, Double kmRecorridos);

    ViajeResponseDTO iniciarPausa(String id);

    ViajeResponseDTO finalizarPausa(String id);

    ViajeResponseDTO getViajeById(String id);

    List<ViajeResponseDTO> getAllViajes();

    List<ViajeResponseDTO> getViajesByUsuario(Long usuarioId);

    List<ViajeResponseDTO> getViajesByMonopatin(Long monopatinId);

    ReporteUsoMonopatinDTO getReporteUsoMonopatin(Long monopatinId, boolean incluirPausas);

    Long contarViajesPorMonopatinEnAnio(Long monopatinId, Integer anio);

    List<ViajeResponseDTO> getViajesByUsuarioEnPeriodo(Long usuarioId, LocalDateTime inicio, LocalDateTime fin);

    void deleteViaje(String id);
}
