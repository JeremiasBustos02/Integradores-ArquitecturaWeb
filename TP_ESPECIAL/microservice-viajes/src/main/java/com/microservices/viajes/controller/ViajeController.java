package com.microservices.viajes.controller;

import com.microservices.viajes.dto.request.FinalizarViajeRequestDTO;
import com.microservices.viajes.dto.request.ViajeRequestDTO;
import com.microservices.viajes.dto.response.ReporteUsoMonopatinDTO;
import com.microservices.viajes.dto.response.ViajeResponseDTO;
import com.microservices.viajes.entity.Viaje;
import com.microservices.viajes.service.ViajeServiceI;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/api/viaje")
public class ViajeController {
    private final ViajeServiceI viajeService;

    @PostMapping
    public ResponseEntity<ViajeResponseDTO> createViaje(@RequestBody @Valid ViajeRequestDTO request) {
        log.info("REST Request to create viaje con parada en: {}", request.getParadaInicioId());

        ViajeResponseDTO viaje = viajeService.createViaje(
                request.getMonopatinId(), request.getParadaInicioId(), request.getUsuarioId(), request.getTarifaId()
        );
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/viaje/{id}")
                .buildAndExpand(viaje.getId())
                .toUri();

        return ResponseEntity.created(location).body(viaje);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ViajeResponseDTO> finalizarViaje(
            @PathVariable @NotNull String id,
            @RequestBody @Valid FinalizarViajeRequestDTO request) {

        log.info("REST request to finalice viaje: {}", id);

        ViajeResponseDTO viaje = viajeService.finalizarViaje(
                id,
                request.getParadaFin(),
                request.getKmRecorridos()
        );

        return ResponseEntity.ok().body(viaje);
    }

    @PostMapping("/{id}/pausa/iniciar")
    public ResponseEntity<ViajeResponseDTO> iniciarPausa(@PathVariable @NotNull String id) {
        return ResponseEntity.ok(viajeService.iniciarPausa(id));
    }

    @PutMapping("/{id}/pausa/finalizar")
    public ResponseEntity<ViajeResponseDTO> finalizarPausa(@PathVariable @NotNull String id) {
        return ResponseEntity.ok(viajeService.finalizarPausa(id));
    }

    @GetMapping("reportes/monopatin/{monopatinid}")
    public ResponseEntity<ReporteUsoMonopatinDTO> getReportePorMonopatin(@PathVariable @NotNull Long monopatinid,
                                                                         @RequestParam(defaultValue = "true") boolean incluirPausas) {
        return ResponseEntity.ok(viajeService.getReporteUsoMonopatin(monopatinid, incluirPausas));
    }

    @GetMapping("reportes/viajes/{monopatinid}/conteo-anual")
    public ResponseEntity<Long> getViajesAnualesPorMonopatin(@PathVariable @NotNull Long monopatinid, @RequestParam Integer anio) {
        return ResponseEntity.ok(viajeService.contarViajesPorMonopatinEnAnio(monopatinid, anio));
    }

    @GetMapping("reportes/viajes/usuario/{usuarioId}")
    public ResponseEntity<List<ViajeResponseDTO>> getViajesPorUsuario(@PathVariable @NotNull Long usuarioId) {
        return ResponseEntity.ok(viajeService.getViajesByUsuario(usuarioId));
    }
}
