package com.microservices.viajes.clients;

import com.microservices.viajes.dto.request.EstadosFactura;
import com.microservices.viajes.dto.request.EstadosMonopatin;
import com.microservices.viajes.dto.response.MonopatinDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "microservice-monopatin")
public interface MonopatinClientRest {
    @GetMapping("/api/monopatin/{id}")
    MonopatinDTO getMonopatinById(@PathVariable("id") Long id);

    //actualizar estado monopatin
    @PutMapping("api/monopatin/{id}/estado")
    MonopatinDTO actualizarEstado(@PathVariable("id") Long id, @RequestParam EstadosMonopatin estado);
}
