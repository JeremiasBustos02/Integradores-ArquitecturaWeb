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
    @NotNull(message = "El ID de cuenta es obligatorio")
    private Long cuentaId;

    private Long viajeId;

    @NotNull(message = "El monto total es obligatorio")
    @Positive(message = "El monto total debe ser positivo")
    private Double montoTotal;

    @NotNull(message = "La fecha de emisión es obligatoria")
    private LocalDateTime fechaEmision;

    private LocalDate fechaVencimiento;

    @NotNull(message = "El estado es obligatorio")
    private EstadosFactura estado;

    private String descripcion;

    private Integer periodoMes;

    private Integer periodoAnio;

    private String tipoCuenta;
}
