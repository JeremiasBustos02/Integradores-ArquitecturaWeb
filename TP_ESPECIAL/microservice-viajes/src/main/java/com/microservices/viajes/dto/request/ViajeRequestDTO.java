package com.microservices.viajes.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ViajeRequestDTO {
    @NotNull(message = "El monopatín es obligatorio")
    @Positive(message = "El ID del monopatín debe ser positivo")
    private Long monopatinId;

    @NotNull(message = "El usuario es obligatorio")
    @Positive
    private Long usuarioId;

    @NotNull(message = "La tarifa es obligatoria")
    @Positive
    private Long tarifaId;

    @NotNull(message = "La parada de inicio es obligatoria")
    @Positive
    private Long paradaInicioId;
}
