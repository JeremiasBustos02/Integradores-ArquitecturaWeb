package com.microservices.facturacion.dto.request;

import com.microservices.facturacion.entity.EstadoFactura;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
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
    private EstadoFactura estado;

    private String descripcion;

    private Integer periodoMes;

    private Integer periodoAnio;

    private String tipoCuenta;
}