package com.microservices.microservicemonopatin.controller;

import com.microservices.microservicemonopatin.dto.MonopatinCercanoDTO;
import com.microservices.microservicemonopatin.dto.MonopatinDTO;
import com.microservices.microservicemonopatin.dto.MonopatinRequestDTO;
import com.microservices.microservicemonopatin.dto.ReporteKilometrosDTO;
import com.microservices.microservicemonopatin.entity.EstadoMonopatin;
import com.microservices.microservicemonopatin.service.MonopatinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/monopatines")
@RequiredArgsConstructor
public class MonopatinController {

    private final MonopatinService monopatinService;

    // ==================== CRUD BÁSICO ====================

    @PostMapping
    public ResponseEntity<MonopatinDTO> crear(@Valid @RequestBody MonopatinRequestDTO monopatinDTO) {
        MonopatinDTO creado = monopatinService.crear(monopatinDTO);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonopatinDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(monopatinService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<MonopatinDTO>> obtenerTodos() {
        return ResponseEntity.ok(monopatinService.obtenerTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MonopatinDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MonopatinRequestDTO monopatinDTO) {
        return ResponseEntity.ok(monopatinService.actualizar(id, monopatinDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        monopatinService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== GESTIÓN DE MANTENIMIENTO ====================

    @PutMapping("/{id}/mantenimiento")
    public ResponseEntity<MonopatinDTO> marcarEnMantenimiento(@PathVariable Long id) {
        return ResponseEntity.ok(monopatinService.marcarEnMantenimiento(id));
    }

    @PutMapping("/{id}/mantenimiento/finalizar")
    public ResponseEntity<MonopatinDTO> sacarDeMantenimiento(@PathVariable Long id) {
        return ResponseEntity.ok(monopatinService.sacarDeMantenimiento(id));
    }

    // ==================== GESTIÓN DE ESTADOS ====================

    @PutMapping("/{id}/estado")
    public ResponseEntity<MonopatinDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoMonopatin estado) {
        return ResponseEntity.ok(monopatinService.cambiarEstado(id, estado));
    }

    // ==================== BÚSQUEDA Y UBICACIÓN ====================

    /**
     * g. Como usuario quiero buscar un listado de los monopatines cercanos a mi zona
     */
    @GetMapping("/cercanos")
    public ResponseEntity<List<MonopatinCercanoDTO>> buscarCercanos(
            @RequestParam Double latitud,
            @RequestParam Double longitud,
            @RequestParam(defaultValue = "5.0") Double radioKm) {
        return ResponseEntity.ok(monopatinService.buscarCercanos(latitud, longitud, radioKm));
    }

    @PutMapping("/{id}/ubicacion")
    public ResponseEntity<Void> actualizarUbicacion(
            @PathVariable Long id,
            @RequestParam Double latitud,
            @RequestParam Double longitud) {
        monopatinService.actualizarUbicacion(id, latitud, longitud);
        return ResponseEntity.noContent().build();
    }

    // ==================== REPORTES ====================

    /**
     * a. Como administrador quiero poder generar un reporte de uso de monopatines por kilómetros
     */
    @GetMapping("/reporte/kilometros")
    public ResponseEntity<List<ReporteKilometrosDTO>> reporteKilometros(
            @RequestParam(defaultValue = "false") boolean incluirPausas) {
        return ResponseEntity.ok(monopatinService.reporteKilometros(incluirPausas));
    }

    /**
     * c. Como administrador quiero consultar los monopatines con más de X viajes en un cierto año
     */
    @GetMapping("/reporte/viajes")
    public ResponseEntity<List<MonopatinDTO>> obtenerConMasDeXViajes(
            @RequestParam int cantidad) {
        return ResponseEntity.ok(monopatinService.obtenerConMasDeXViajes(cantidad));
    }

    // ==================== USO Y VIAJES ====================

    /**
     * Endpoint interno para que el microservicio de viajes actualice el uso del monopatín
     */
    @PutMapping("/{id}/registrar-uso")
    public ResponseEntity<Void> registrarUsoDeViaje(
            @PathVariable Long id,
            @RequestParam Double kmRecorridos,
            @RequestParam Long tiempoUso,
            @RequestParam Long tiempoPausas) {
        monopatinService.registrarUsoDeViaje(id, kmRecorridos, tiempoUso, tiempoPausas);
        return ResponseEntity.noContent().build();
    }
}