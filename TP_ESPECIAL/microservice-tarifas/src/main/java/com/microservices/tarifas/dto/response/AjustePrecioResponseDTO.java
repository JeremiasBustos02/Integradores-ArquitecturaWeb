package com.microservices.tarifas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AjustePrecioResponseDTO {
    private Long id;
    private Double precioKm;
    private Double precioMin;
    private Double extraPausaMultiplicador;
    private OffsetDateTime efectivaDesde;
    private String estado;
}

