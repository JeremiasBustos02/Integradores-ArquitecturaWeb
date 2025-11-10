package com.microservices.viajes.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ViajeRequestDTO {
    private Long monopatinId;
    private Long usuarioId;
    private Long tarifaId;
    private Long paradaInicioId;
}
