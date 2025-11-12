package com.monopatines.admin.client;
import com.monopatines.admin.dto.TotalFacturadoDTO; 
import org.springframework.cloud.openfeign.FeignClient; 
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="microservice-facturacion")
public interface FacturacionClient {
    @GetMapping("/facturacion/total")
    TotalFacturadoDTO total(@RequestParam int anio, @RequestParam int mesDesde, @RequestParam int mesHasta);
}