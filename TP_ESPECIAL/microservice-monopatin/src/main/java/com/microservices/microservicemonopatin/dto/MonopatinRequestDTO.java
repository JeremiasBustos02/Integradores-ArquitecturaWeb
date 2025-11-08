package com.microservices.microservicemonopatin.dto;

import com.microservices.microservicemonopatin.entity.EstadoMonopatin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonopatinRequestDTO {

    @NotNull(message = "El ID del monopatín es obligatorio")
    private Long id;

    @NotNull(message = "La latitud es obligatoria")
    private Double latitud;

    @NotNull(message = "La longitud es obligatoria")
    private Double longitud;

    @NotNull(message = "El estado del monopatín es obligatorio")
    private EstadoMonopatin estado;

    private Long idParadaActual;
}