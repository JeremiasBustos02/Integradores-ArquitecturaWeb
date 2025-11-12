package com.monopatines.admin.client;

import com.monopatines.admin.dto.MonopatinResumenDTO; 
import com.monopatines.admin.dto.UsoMonopatinDTO;
import org.springframework.cloud.openfeign.FeignClient; 
import org.springframework.format.annotation.DateTimeFormat; 
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate; 
import java.util.List;

@FeignClient(name="microservice-monopatin")
public interface MonopatinClient {
    @GetMapping("/monopatines/reportes/uso")
    List<UsoMonopatinDTO> reporteUso(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate desde,
                                  @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate hasta,
                                  @RequestParam(defaultValue="false") boolean incluirPausas);

    @GetMapping("/monopatines/reportes/viajes-anuales")
    
    List<MonopatinResumenDTO> monopatinesConMasViajes(@RequestParam int anio, @RequestParam int minViajes);
}