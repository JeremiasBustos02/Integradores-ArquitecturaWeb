package com.microservices.viajes.dto.response;

import com.microservices.viajes.dto.request.EstadosFactura;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacturaResponseDTO {
    @NotNull
    private Long id;
    @NotNull
    private Long cuentaId;
    @NotNull
    private String viajeId;
    @NotNull
    @Positive
    private Double montoTotal;
    @NotNull
    private LocalDateTime fechaEmision;
    private EstadosFactura estado;
}
