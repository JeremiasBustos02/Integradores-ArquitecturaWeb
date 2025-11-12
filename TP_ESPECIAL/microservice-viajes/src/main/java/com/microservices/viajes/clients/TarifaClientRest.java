package com.microservices.viajes.clients;

import com.microservices.viajes.dto.response.TarifaDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservice-tarifas")
public interface TarifaClientRest {
    @GetMapping("/api/tarifas/{id}")
    TarifaDTO getTarifaById(@PathVariable @NotNull Long id);

}
