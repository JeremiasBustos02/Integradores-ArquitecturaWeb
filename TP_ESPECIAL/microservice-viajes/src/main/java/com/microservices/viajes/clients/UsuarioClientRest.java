package com.microservices.viajes.clients;

import com.microservices.viajes.dto.response.CuentaResponseDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "microservice-user")
public interface UsuarioClientRest {

    @GetMapping("/api/usuarios/{id}/cuentas")
    List<CuentaResponseDTO> getCuentasUsuario(@PathVariable("id") Long usuarioId);

    @GetMapping("api/usuarios/{usuarioId}/cuentas/cuenta-para-facturar")
    Long getCuentaFacturar(@PathVariable("usuarioId") Long usuarioId);
}
