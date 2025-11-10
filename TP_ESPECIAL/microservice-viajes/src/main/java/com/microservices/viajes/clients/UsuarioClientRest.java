package com.microservices.viajes.clients;

import com.microservices.viajes.dto.response.UsuarioDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservice-user")
public interface UsuarioClientRest {
    @GetMapping("api/usuarios/{id}")
    UsuarioDTO getUsuarioById(@PathVariable @NotNull Long id);

    @GetMapping("api/usuarios/{id}/puede-viajar")
    boolean saldoSuficiente(@PathVariable @NotNull Long id);
    //me deberia devolver la cuenta que se uso para facturar
    @GetMapping("api/usuarios/{id}")
    UsuarioDTO getCuenta(@PathVariable @NotNull Long id);
    @GetMapping("/api/usuarios/{id}/cuenta-para-facturar")
    Long getCuentaParaFacturar(@PathVariable("id") Long usuarioId);
}
