package com.microservices.viajes.clients;

import com.microservices.viajes.dto.response.ParadaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "microservice-parada")
public interface ParadaClientRest {
    @GetMapping("/api/paradas/{id}")
    ParadaDTO getParadaById(@PathVariable("id") Long id);
    @PostMapping("/api/paradas/batch")
    List<ParadaDTO> getParadasByIds(@RequestBody List<Long> ids);
}
