package com.microservices.usuarios.dto.request;

import com.microservices.usuarios.entity.TipoCuenta;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaRequestDTO {
    
    @NotNull(message = "El ID de cuenta de Mercado Pago es obligatorio")
    @Positive(message = "El ID de cuenta de Mercado Pago debe ser positivo")
    private Long idCuentaMercadoPago;
    
    private TipoCuenta tipoCuenta = TipoCuenta.BASICA;
    
    private BigDecimal saldoInicial = BigDecimal.ZERO;
}
