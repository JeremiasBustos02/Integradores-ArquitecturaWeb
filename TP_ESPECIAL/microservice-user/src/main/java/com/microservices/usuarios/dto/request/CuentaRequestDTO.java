package com.microservices.usuarios.dto.request;

import com.microservices.usuarios.entity.TipoCuenta;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Id de la cuenta de mercadoPago", example = "130")
    @NotNull(message = "El ID de cuenta de Mercado Pago es obligatorio")
    @Positive(message = "El ID de cuenta de Mercado Pago debe ser positivo")
    private Long idCuentaMercadoPago;
    @Schema(description = "Tipo de cuenta a crear", example = "PREMIUM")
    private TipoCuenta tipoCuenta = TipoCuenta.BASICA;
    @Schema(description = "Saldo de la cuenta inicial", example = "200")
    private BigDecimal saldoInicial = BigDecimal.ZERO;
}
