package com.microservices.facturacion.client;

import com.microservices.facturacion.dto.response.TarifaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(name = "microservice-tarifas")
public interface TarifaFeignClient {
    
    @GetMapping("/api/tarifas/activa")
    TarifaResponseDTO obtenerTarifaActiva();
    
    @GetMapping("/api/tarifas/vigente")
    TarifaResponseDTO obtenerTarifaVigenteEnFecha(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha);
}

