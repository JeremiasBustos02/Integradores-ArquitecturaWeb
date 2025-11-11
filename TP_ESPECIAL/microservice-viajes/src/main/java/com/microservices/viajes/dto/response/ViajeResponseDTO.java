package com.microservices.viajes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ViajeResponseDTO {
    private String id;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private double kmRecorridos;
    private ParadaDTO paradaInicio;
    private ParadaDTO paradaFin;
    private boolean pausaExtensa;
    private Long monopatinId;
    private Long usuarioId;
    private Long tarifaId;
    private List<PausaDTO> pausas;
}