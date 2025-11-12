package com.monopatines.admin.client;

import com.monopatines.admin.dto.PageDTO;
import com.monopatines.admin.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient; 
import org.springframework.web.bind.annotation.*;

@FeignClient(name="microservice-user")
public interface UserClient {
    
    @GetMapping("/api/usuarios?size=1000")
    PageDTO<UsuarioDTO> getAllUsuarios();
    
    @GetMapping("/api/usuarios/{id}")
    UsuarioDTO getUsuarioById(@PathVariable Long id);
    
    @PatchMapping("/api/cuentas/{id}/deshabilitar")
    void anularCuenta(@PathVariable Long id);
}