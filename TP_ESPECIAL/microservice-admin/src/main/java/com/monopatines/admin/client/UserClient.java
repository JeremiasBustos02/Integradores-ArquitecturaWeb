package com.monopatines.admin.client;

import com.monopatines.admin.dto.UsuarioUsoDTO; 
import org.springframework.cloud.openfeign.FeignClient; 
import org.springframework.format.annotation.DateTimeFormat; 
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate; 
import java.util.List;

@FeignClient(name="microservice-user")
public interface UserClient {
    @PatchMapping("/api/usuarios/{id}/anular") 
    void anular(@PathVariable Long id);
 
    @GetMapping("/api/usuarios/uso")
    List<UsuarioUsoDTO> usuariosMasFrecuentes(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate desde,
                                           @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate hasta,
                                           @RequestParam(required=false) String tipo);
}