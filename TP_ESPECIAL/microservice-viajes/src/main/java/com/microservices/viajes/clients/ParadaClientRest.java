package com.microservices.viajes.clients;

import com.microservices.viajes.dto.response.ParadaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservice-parada")
public interface ParadaClientRest {
    @GetMapping("/api/parada/{id}")
    ParadaDTO getParadaById(@PathVariable("id") Long id);
}
