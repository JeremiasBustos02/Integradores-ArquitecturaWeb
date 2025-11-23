package com.microservices.viajes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FinalizarViajeRequestDTO {
    @Schema(description = "Cantidad total de los kilometros recorridos",example = "15.0")
    @NotNull(message = "Los kilometros no pueden ser nulos")
    @PositiveOrZero(message = "Los kilometros deben ser 0 o mas")
    private Double kmRecorridos;
    @Schema(description = "Id de la parada final",example = "3")
    @NotNull(message = "La parada de fin no puede ser nula")
    private Long paradaFin;
}
