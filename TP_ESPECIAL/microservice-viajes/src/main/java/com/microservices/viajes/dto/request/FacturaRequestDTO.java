package com.microservices.viajes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Id de la cuenta a facturar", example = "1")
    @NotNull(message = "El ID de cuenta es obligatorio")
    private Long cuentaId;
    @Schema(description = "Id del viaje a facturar", example = "200")

    private String viajeId;

    @Schema(description = "Monto total del viaje para la factura", example = "200.0")

    @NotNull(message = "El monto total es obligatorio")
    @Positive(message = "El monto total debe ser positivo")
    private Double montoTotal;
    @Schema(description = "Fecha de emision de la factura", example = "2025-11-22T15:30:00")
    @NotNull(message = "La fecha de emisión es obligatoria")
    private LocalDateTime fechaEmision;
    @Schema(description = "Fecha de vencimiento de la factura", example = "2025-12-01")

    private LocalDate fechaVencimiento;
    @Schema(description = "Estado de la factura", example = "PENDIENTE")

    @NotNull(message = "El estado es obligatorio")
    private EstadosFactura estado;


}
