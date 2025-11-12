package com.microservices.tarifas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrecioVigenteRequestDTO {
    
    @NotNull(message = "El precio por kilómetro es obligatorio")
    @Positive(message = "El precio por kilómetro debe ser mayor a cero")
    private Double precioKm;

    @NotNull(message = "El precio por minuto es obligatorio")
    @Positive(message = "El precio por minuto debe ser mayor a cero")
    private Double precioMin;

    @NotBlank(message = "La moneda es obligatoria")
    private String moneda;

    private OffsetDateTime vigenteDesde;
    private OffsetDateTime vigenteHasta;
}

