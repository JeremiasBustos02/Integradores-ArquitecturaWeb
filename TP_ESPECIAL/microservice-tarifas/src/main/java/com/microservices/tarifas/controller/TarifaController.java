package com.microservices.tarifas.controller;

import com.microservices.tarifas.dto.request.AjustePrecioRequestDTO;
import com.microservices.tarifas.dto.request.PrecioVigenteRequestDTO;
import com.microservices.tarifas.dto.request.TarifaRequestDTO;
import com.microservices.tarifas.dto.response.AjustePrecioResponseDTO;
import com.microservices.tarifas.dto.response.PrecioVigenteResponseDTO;
import com.microservices.tarifas.dto.response.TarifaResponseDTO;
import com.microservices.tarifas.service.IPrecioService;
import com.microservices.tarifas.service.ITarifaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tarifas")
@RequiredArgsConstructor
@Validated
@Slf4j
public class TarifaController {
    
    private final ITarifaService tarifaService;
    private final IPrecioService precioService;
    
    @PostMapping
    public ResponseEntity<TarifaResponseDTO> crearTarifa(@Valid @RequestBody TarifaRequestDTO requestDTO) {
        TarifaResponseDTO tarifa = tarifaService.crearTarifa(requestDTO);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tarifa.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(tarifa);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TarifaResponseDTO> actualizarTarifa(
            @PathVariable Long id,
            @Valid @RequestBody TarifaRequestDTO requestDTO) {
        TarifaResponseDTO tarifa = tarifaService.actualizarTarifa(id, requestDTO);
        return ResponseEntity.ok(tarifa);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TarifaResponseDTO> obtenerTarifaPorId(@PathVariable Long id) {
        TarifaResponseDTO tarifa = tarifaService.obtenerTarifaPorId(id);
        return ResponseEntity.ok(tarifa);
    }
    
    @GetMapping
    public ResponseEntity<List<TarifaResponseDTO>> obtenerTodasLasTarifas() {
        List<TarifaResponseDTO> tarifas = tarifaService.obtenerTodasLasTarifas();
        return ResponseEntity.ok(tarifas);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarifa(@PathVariable Long id) {
        tarifaService.eliminarTarifa(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/activa")
    public ResponseEntity<TarifaResponseDTO> obtenerTarifaActiva() {
        TarifaResponseDTO tarifa = tarifaService.obtenerTarifaActiva();
        return ResponseEntity.ok(tarifa);
    }
    
    @GetMapping("/vigente")
    public ResponseEntity<TarifaResponseDTO> obtenerTarifaVigenteEnFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        TarifaResponseDTO tarifa = tarifaService.obtenerTarifaVigenteEnFecha(fecha);
        return ResponseEntity.ok(tarifa);
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<TarifaResponseDTO>> obtenerTarifasPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<TarifaResponseDTO> tarifas = tarifaService.obtenerTarifasPorRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(tarifas);
    }

    // ==================== GESTIÓN DE PRECIOS VIGENTES ====================

    /**
     * f. Como administrador quiero definir precios
     */
    @GetMapping("/precios")
    public ResponseEntity<List<PrecioVigenteResponseDTO>> listarPreciosVigentes() {
        log.info("REST request to get all precios vigentes");
        return ResponseEntity.ok(precioService.listarVigentes());
    }

    @PostMapping("/precios")
    public ResponseEntity<PrecioVigenteResponseDTO> definirPrecioVigente(@Valid @RequestBody PrecioVigenteRequestDTO dto) {
        log.info("REST request to create precio vigente");
        PrecioVigenteResponseDTO created = precioService.definirVigente(dto);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(created);
    }

    /**
     * f. Como administrador quiero programar ajustes de precios
     */
    @PostMapping("/precios/ajustes")
    public ResponseEntity<AjustePrecioResponseDTO> programarAjustePrecio(@Valid @RequestBody AjustePrecioRequestDTO dto) {
        log.info("REST request to program price adjustment");
        AjustePrecioResponseDTO created = precioService.programarAjuste(dto);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(created);
    }
}

