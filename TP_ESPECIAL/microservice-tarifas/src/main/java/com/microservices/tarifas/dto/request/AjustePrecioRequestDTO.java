package com.microservices.tarifas.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AjustePrecioRequestDTO {
    @Schema(description = "Ajustar el precio de la tarifa por kilometro", example = "250.0")
    @NotNull(message = "El precio por kilómetro es obligatorio")
    @Positive(message = "El precio por kilómetro debe ser mayor a cero")
    private Double precioKm;
    @Schema(description = "Ajustar precio por minuto de la tarifa", example = "60.0")
    @NotNull(message = "El precio por minuto es obligatorio")
    @Positive(message = "El precio por minuto debe ser mayor a cero")
    private Double precioMin;
    @Schema(description = "Ajustar precio de la pausa", example = "1.5")
    @NotNull(message = "El multiplicador de pausa extra es obligatorio")
    @Positive(message = "El multiplicador debe ser mayor a cero")
    private Double extraPausaMultiplicador;
    @Schema(description = "Fecha limite de pago (formato ISO)", example = "2025-12-01T08:00:00Z")
    @NotNull(message = "La fecha efectiva es obligatoria")
    @Future(message = "La fecha efectiva debe ser futura")
    private OffsetDateTime efectivaDesde;
}

