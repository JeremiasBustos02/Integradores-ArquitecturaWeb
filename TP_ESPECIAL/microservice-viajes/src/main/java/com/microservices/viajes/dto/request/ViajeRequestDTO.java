package com.microservices.viajes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Id del monopatin que va a ser utilizado", example = "2")
    @NotNull(message = "El monopatín es obligatorio")
    @Positive(message = "El ID del monopatín debe ser positivo")
    private Long monopatinId;
    @Schema(description = "Id del usuario que va a viajar", example = "100")
    @NotNull(message = "El usuario es obligatorio")
    @Positive
    private Long usuarioId;
    @Schema(description = "Id de la tarifa asociada al viaje", example = "5")
    @NotNull(message = "La tarifa es obligatoria")
    @Positive
    private Long tarifaId;
    @Schema(description = "Id de la parada desde la que inicia el viaje", example = "1")
    @NotNull(message = "La parada de inicio es obligatoria")
    @Positive
    private Long paradaInicioId;
}
