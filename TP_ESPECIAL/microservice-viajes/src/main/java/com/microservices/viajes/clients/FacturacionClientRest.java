package com.microservices.viajes.clients;

import com.microservices.viajes.dto.request.FacturaRequestDTO;
import com.microservices.viajes.dto.response.FacturaResponseDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "microservice-facturacion")
public interface FacturacionClientRest {
    @PostMapping("api/facturas")
    FacturaResponseDTO crearFactura(@Valid @RequestBody FacturaRequestDTO factura);
}
