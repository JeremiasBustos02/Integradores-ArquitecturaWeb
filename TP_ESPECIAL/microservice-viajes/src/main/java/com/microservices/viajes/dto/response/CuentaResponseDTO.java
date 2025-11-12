package com.microservices.viajes.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter

public class CuentaResponseDTO {
    private Long id;
    private Long idCuentaMercadoPago;
    private LocalDateTime fechaAlta;
    private Boolean estadoCuenta;
    private TipoCuenta tipoCuenta;
    private BigDecimal saldo;
    private Double kilometrosRecorridosMes;
    private LocalDateTime fechaRenovacionCupo;
}
