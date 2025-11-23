package com.microservices.microservicemonopatin.dto;

import com.microservices.microservicemonopatin.entity.EstadoMonopatin;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonopatinRequestDTO {
    @Schema(description = "Latitud del monopatin", example = "-37.3217")
    @NotNull(message = "La latitud es obligatoria")
    private Double latitud;
    @Schema(description = "Longitud del monopatin", example = "-59.1332")

    @NotNull(message = "La longitud es obligatoria")
    private Double longitud;
    @Schema(description = "Estado del monopatin", example = "EN_USO")
    @NotNull(message = "El estado del monopatín es obligatorio")
    private EstadoMonopatin estado;
    @Schema(description = "Id de la parada actual del monopatin", example = "1")
    private Long idParadaActual;
}