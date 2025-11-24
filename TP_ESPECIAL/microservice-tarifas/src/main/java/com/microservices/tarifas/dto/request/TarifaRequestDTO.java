package com.microservices.tarifas.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifaRequestDTO {
    @Schema(description = "Monto base de la tarifa", example = "50.0")
    @NotNull(message = "El monto base es obligatorio")
    @Positive(message = "El monto base debe ser positivo")
    private Double montoBase;
    @Schema(description = "Monto extra de la tarifa", example = "100.0")
    @NotNull(message = "El monto extra es obligatorio")
    @Positive(message = "El monto extra debe ser positivo")
    private Double montoExtra;
    @Schema(description = "Vigencia de la tarifa", example = "2025-01-01")
    @NotNull(message = "La fecha de vigencia es obligatoria")
    private LocalDate fechaVigencia;
    @Schema(description = "Indica si la tarifa está activa", example = "true")
    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activa;
    @Schema(description = "Descripcion de la tarifa", example = "Tarifa Inicial 2025")
    private String descripcion;
}

