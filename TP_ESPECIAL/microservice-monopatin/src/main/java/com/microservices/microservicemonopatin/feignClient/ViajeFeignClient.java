package com.microservices.microservicemonopatin.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Cliente Feign para comunicarse con el microservicio de Viajes
 */
@FeignClient(name = "microservice-viaje", url = "${microservice.viaje.url:http://localhost:8082}")
public interface ViajeFeignClient {

    /**
     * Obtiene la cantidad de viajes de un monopatín específico
     *
     * @param monopatinId ID del monopatín
     * @return cantidad de viajes realizados
     */
    @GetMapping("/api/viajes/monopatin/{monopatinId}/cantidad")
    Integer contarViajesPorMonopatin(@PathVariable Long monopatinId);

    /**
     * Obtiene la cantidad de viajes de un monopatín en un año específico
     *
     * @param monopatinId ID del monopatín
     * @param anio        año a consultar
     * @return cantidad de viajes en ese año
     */
    @GetMapping("/api/viajes/monopatin/{monopatinId}/cantidad/anio")
    Integer contarViajesPorMonopatinYAnio(
            @PathVariable Long monopatinId,
            @RequestParam int anio);
}