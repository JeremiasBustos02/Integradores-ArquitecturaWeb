package com.microservices.viajes.clients;

import com.microservices.viajes.dto.response.UsuarioDTO;
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

@FeignClient(name = "microservice-user")
public interface UsuarioClientRest {
    @GetMapping("api/usuarios/{id}")
    UsuarioDTO getUsuarioById(@PathVariable @NotNull Long id);

    @GetMapping("/api/usuarios/{id}/cuenta-para-facturar")
    Long getCuentaParaFacturar(@PathVariable("id") Long usuarioId);
}
