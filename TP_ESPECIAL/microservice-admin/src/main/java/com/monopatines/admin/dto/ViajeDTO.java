package com.monopatines.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViajeDTO {
    private String id;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private Double distanciaRecorrida;
    private Long usuarioId;
    private Long monopatinId;
}

