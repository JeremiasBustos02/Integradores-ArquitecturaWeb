package com.microservices.viajes.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CuentaBasicResponseDTO {
    private Long id;
    private Long idCuentaMercadoPago;
    private LocalDateTime fechaAlta;
    private Boolean estadoCuenta;
    private TipoCuenta tipoCuenta;
    private BigDecimal saldo;
}
