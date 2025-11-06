package com.microservices.usuarios.dto.response;

import com.microservices.usuarios.entity.TipoCuenta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaResponseDTO {
    private Long id;
    private Long idCuentaMercadoPago;
    private LocalDateTime fechaAlta;
    private Boolean estadoCuenta;
    private TipoCuenta tipoCuenta;
    private BigDecimal saldo;
    private Double kilometrosRecorridosMes;
    private LocalDateTime fechaRenovacionCupo;
    private Set<UsuarioBasicResponseDTO> usuarios;
}
