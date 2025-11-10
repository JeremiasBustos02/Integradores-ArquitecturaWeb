package com.microservices.viajes.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacturaRequestDTO {
    @NotNull
    private Long cuentaId;

    @NotNull
    private String viajeId; // String porque es el ID de Mongo

    @NotNull
    @Positive
    private Double montoTotal;

    @NotNull
    private LocalDateTime fechaEmision;

    @NotNull
    private EstadosFactura estado;
}
