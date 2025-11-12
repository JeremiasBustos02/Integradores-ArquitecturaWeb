package com.microservices.facturacion.dto.response;

import com.microservices.facturacion.entity.EstadoFactura;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturaResponseDTO {

    private Long id;
    private String numeroFactura;
    private Long cuentaId;
    private Long viajeId;
    private Double montoTotal;
    private LocalDateTime fechaEmision;
    private LocalDate fechaVencimiento;
    private EstadoFactura estado;
    private String descripcion;
    private Integer periodoMes;
    private Integer periodoAnio;
    private String tipoCuenta;
}