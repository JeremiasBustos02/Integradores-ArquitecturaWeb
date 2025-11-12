package com.microservices.tarifas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrecioVigenteResponseDTO {
    private Long id;
    private Double precioKm;
    private Double precioMin;
    private String moneda;
    private OffsetDateTime vigenteDesde;
    private OffsetDateTime vigenteHasta;
    private Boolean activo;
}

