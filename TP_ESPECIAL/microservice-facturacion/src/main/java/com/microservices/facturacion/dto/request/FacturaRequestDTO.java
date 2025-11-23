package com.microservices.facturacion.dto.request;

import com.microservices.facturacion.entity.EstadoFactura;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Schema(description = "Id de la cuenta a la cual se le va a facturar", example = "2")
    @NotNull(message = "El ID de cuenta es obligatorio")
    private Long cuentaId;

    @Schema(description = "Id del viaje que se va a facturar", example = "3000")
    private String viajeId;

    @Schema(description = "Total del monto a cobrar", example = "1500")
    @NotNull(message = "El monto total es obligatorio")
    private Double montoTotal;

    @Schema(description = "Fecha de la emision de la factura", example = "2025-02-21T10:30:00")
    @NotNull(message = "La fecha de emisión es obligatoria")
    private LocalDateTime fechaEmision;

    @Schema(description = "Fecha de vencimiento de la factura", example = "2025-02-25")
    private LocalDate fechaVencimiento;
    @Schema(description = "Estado de el pago de la factura", example = "PENDIENTE")

    @NotNull(message = "El estado es obligatorio")
    private EstadoFactura estado;
    @Schema(description = "Descripcion de el viaje", example = "Parada inicial: plaza. Parada final: Centro. Distancia Recorrida..")

    private String descripcion;
    @Schema(description = "Mes del periodo facturado (1-12)", example = "2")
    @Min(1)
    @Max(12)
    private Integer periodoMes;
    @Schema(description = "Año del periodo facturado", example = "2025")
    private Integer periodoAnio;
    @Schema(description = "Calidad de la cuenta", example = "Premium")

    private String tipoCuenta;
}