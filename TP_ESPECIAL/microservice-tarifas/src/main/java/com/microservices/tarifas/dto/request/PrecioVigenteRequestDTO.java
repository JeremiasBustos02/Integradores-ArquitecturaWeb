package com.microservices.tarifas.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Precio actual del kilometro en tarifa", example = "200.0")
    @NotNull(message = "El precio por kilómetro es obligatorio")
    @Positive(message = "El precio por kilómetro debe ser mayor a cero")
    private Double precioKm;
    @Schema(description = "Precio actual por minuto de la tarifa", example = "50.0")
    @NotNull(message = "El precio por minuto es obligatorio")
    @Positive(message = "El precio por minuto debe ser mayor a cero")
    private Double precioMin;
    @Schema(description = "Nombre de la monea corriente", example = "ARS")

    @NotBlank(message = "La moneda es obligatoria")
    private String moneda;
    @Schema(description = "Vigencia del precio de la tarifa", example = "2025-01-01T00:00:00Z")
    private OffsetDateTime vigenteDesde;
    @Schema(description = "Vencimiento del precio de la tarifa", example = "2025-12-31T23:59:59Z")
    private OffsetDateTime vigenteHasta;
}

