package com.monopatines.admin.client;

import com.monopatines.admin.dto.ViajeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "microservice-viajes")
public interface ViajesClient {

    @GetMapping("/api/viajes/reportes/usuario/{usuarioId}/periodo")
    List<ViajeDTO> getViajesPorUsuarioEnPeriodo(
            @PathVariable Long usuarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin);
}

