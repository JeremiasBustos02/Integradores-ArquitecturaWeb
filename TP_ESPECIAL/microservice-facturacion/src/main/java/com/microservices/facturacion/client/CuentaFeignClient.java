package com.microservices.facturacion.client;

import com.microservices.facturacion.dto.response.CuentaResponseDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "microservice-user")
public interface CuentaFeignClient {
    
    @PostMapping("/api/cuentas/{id}/descontar-saldo")
    CuentaResponseDTO descontarSaldo(
            @PathVariable("id") Long id,
            @RequestParam("monto") BigDecimal monto);
}
