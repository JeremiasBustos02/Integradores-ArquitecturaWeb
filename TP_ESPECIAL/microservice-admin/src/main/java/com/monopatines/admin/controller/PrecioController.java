package com.monopatines.admin.controller;
import com.monopatines.admin.dto.AjustePrecioProgramadoDTO; 
import com.monopatines.admin.dto.PrecioVigenteDTO; 
import com.monopatines.admin.service.PrecioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor; 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; 
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*; 
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController 
@RequestMapping("/api/admin/precios") 
@RequiredArgsConstructor
@Validated
public class PrecioController {

    private final PrecioService service;

    @GetMapping 
    public ResponseEntity<List<PrecioVigenteDTO>> listar(){ 
        return ResponseEntity.ok(service.listarVigentes()); 
    }

    @PostMapping 
    public ResponseEntity<PrecioVigenteDTO> definir(@Valid @RequestBody PrecioVigenteDTO dto){ 
        PrecioVigenteDTO created = service.definirVigente(dto);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(created);
    }

    @PostMapping("/ajustes") 
    public ResponseEntity<AjustePrecioProgramadoDTO> programar(@Valid @RequestBody AjustePrecioProgramadoDTO dto){ 
        AjustePrecioProgramadoDTO created = service.programarAjuste(dto);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(created);
    }
}
