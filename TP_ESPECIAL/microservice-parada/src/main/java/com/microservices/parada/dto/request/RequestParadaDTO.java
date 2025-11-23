package com.microservices.parada.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestParadaDTO {
    @Schema(description = "Nombre de la parada", example = "Microcentro")
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    @Schema(description = "Latitud de la parada (Decimal)", example = "-37.3217")
    @NotNull(message = "La latitud es obligatoria")
    private Double latitud;
    @Schema(description = "Longitud de la parada (Decimal)", example = "-59.1332")
    @NotNull(message = "La longitud es obligatoria")
    private Double longitud;

}
