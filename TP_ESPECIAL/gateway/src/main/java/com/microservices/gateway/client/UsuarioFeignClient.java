package com.microservices.gateway.client;

import com.microservices.gateway.dto.UsuarioAuthDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservice-user")
public interface UsuarioFeignClient {
    
    @GetMapping("/api/usuarios/email/{email}/auth")
    UsuarioAuthDTO getUsuarioByEmailForAuth(@PathVariable String email);
}

