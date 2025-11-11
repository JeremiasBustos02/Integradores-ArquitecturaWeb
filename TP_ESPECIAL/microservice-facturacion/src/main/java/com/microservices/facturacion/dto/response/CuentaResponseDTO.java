package com.microservices.facturacion.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public class CuentaResponseDTO {
    private Long id;
    private Long idCuentaMercadoPago;
    private LocalDateTime fechaAlta;
    private Boolean estadoCuenta;
    private BigDecimal saldo;
    private Double kilometrosRecorridosMes;
    private LocalDateTime fechaRenovacionCupo;
}
