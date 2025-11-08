package com.microservices.tarifas.service;

import com.microservices.tarifas.dto.request.TarifaRequestDTO;
import com.microservices.tarifas.dto.response.TarifaResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface ITarifaService {
    
    TarifaResponseDTO crearTarifa(TarifaRequestDTO requestDTO);
    
    TarifaResponseDTO actualizarTarifa(Long id, TarifaRequestDTO requestDTO);
    
    TarifaResponseDTO obtenerTarifaPorId(Long id);
    
    List<TarifaResponseDTO> obtenerTodasLasTarifas();
    
    void eliminarTarifa(Long id);
    
    TarifaResponseDTO obtenerTarifaActiva();
    
    TarifaResponseDTO obtenerTarifaVigenteEnFecha(LocalDate fecha);
    
    TarifaResponseDTO ajustarPreciosDesdeFecha(LocalDate fechaVigencia, Double montoBase, Double montoExtra, String descripcion);
    
    List<TarifaResponseDTO> obtenerTarifasPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);
}

