package com.microservices.viajes.dto.request;

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
    @NotNull(message = "Los kilometros no pueden ser nulos")
    @PositiveOrZero(message = "Los kilometros deben ser 0 o mas")
    private Double kmRecorridos;
    @NotNull(message = "La parada de fin no puede ser nula")
    private Long paradaFin;
}
