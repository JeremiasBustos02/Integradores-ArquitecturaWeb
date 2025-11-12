package com.monopatines.admin.controller;
import com.monopatines.admin.dto.AjustePrecioProgramadoDTO; 
import com.monopatines.admin.dto.PrecioVigenteDTO; 
import com.monopatines.admin.service.PrecioService;
import lombok.RequiredArgsConstructor; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*; 
import java.util.List;

@RestController 
@RequestMapping("/admin/precios") 
@RequiredArgsConstructor
public class PrecioController {

    private final PrecioService service;
    @GetMapping 
    public ResponseEntity<List<PrecioVigenteDTO>> listar(){ 
        return ResponseEntity.ok(service.listarVigentes()); 
    }

    @PostMapping 
    public ResponseEntity<PrecioVigenteDTO> definir(@RequestBody PrecioVigenteDTO dto){ 
        return ResponseEntity.ok(service.definirVigente(dto)); 
    }

    @PostMapping("/ajustes") 
    public ResponseEntity<AjustePrecioProgramadoDTO> programar(@RequestBody AjustePrecioProgramadoDTO dto){ 
        return ResponseEntity.ok(service.programarAjuste(dto)); 
    }
}
