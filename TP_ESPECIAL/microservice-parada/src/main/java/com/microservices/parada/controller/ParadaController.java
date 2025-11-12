package com.microservices.parada.controller;

import com.microservices.parada.dto.response.ResponseParadaDTO;
import com.microservices.parada.dto.request.RequestParadaDTO;
import com.microservices.parada.service.ParadaServiceI;
import com.microservices.parada.service.impl.ParadaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/api/paradas")
public class ParadaController {
    private final ParadaServiceI service;

    @GetMapping("/{id}")
    ResponseEntity<ResponseParadaDTO> getParadaById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(service.getParadaById(id));
    }

    @GetMapping
    ResponseEntity<List<ResponseParadaDTO>> getAllParadas() {
        return ResponseEntity.ok(service.obtenerTodas());
    }

    @PostMapping
    ResponseEntity<ResponseParadaDTO> crearParada(@RequestBody @Valid RequestParadaDTO paradaNueva) {
        // Ahora solo pasas el DTO. La validación se hizo automáticamente
        ResponseParadaDTO parada = service.crearParada(paradaNueva);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/paradas/{id}")
                .buildAndExpand(parada.getId())
                .toUri();
        return ResponseEntity.created(location).body(parada);
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseParadaDTO> actualizarParada(
            @PathVariable @NotNull Long id,
            @RequestBody @Valid RequestParadaDTO paradaDTO) {

        return ResponseEntity.ok(service.actualizarParada(id, paradaDTO));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> borrarParada(@PathVariable @NotNull Long id) {
        service.eliminarParada(id);
        return ResponseEntity.noContent().build();
    }

    //para mapear en viaje
    @PostMapping("/batch")
    ResponseEntity<List<ResponseParadaDTO>> getParadasByIds(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(service.getParadasById(ids));
    }
}
