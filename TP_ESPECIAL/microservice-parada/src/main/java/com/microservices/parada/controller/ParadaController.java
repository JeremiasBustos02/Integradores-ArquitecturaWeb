package com.microservices.parada.controller;

import com.microservices.parada.dto.response.ResponseParadaDTO;
import com.microservices.parada.dto.request.RequestParadaDTO;
import com.microservices.parada.service.ParadaServiceI;
import com.microservices.parada.service.impl.ParadaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Servicio de paradas", description = "Crear, actualizar y eliminacion de paradas")
public class ParadaController {
    private final ParadaServiceI service;

    @Operation(summary = "Obtener parada por id", description = "Devuelve una parada dado un id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parada encontrada"),
            @ApiResponse(responseCode = "404", description = "No se encontró la parada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    ResponseEntity<ResponseParadaDTO> getParadaById(
            @PathVariable @NotNull Long id) {
        return ResponseEntity.ok(service.getParadaById(id));
    }

    @Operation(summary = "Obtener todas las paradas", description = "Devuelve el listado completo de paradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    ResponseEntity<List<ResponseParadaDTO>> getAllParadas() {
        return ResponseEntity.ok(service.obtenerTodas());
    }

    @Operation(summary = "Crear una parada", description = "Creacion de paradas con datos concretos")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Parada creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos insertados no son validos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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

    @Operation(summary = "Actulizar una parada", description = "Actualizar una parada")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Parada actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos insertados no son validos"),
            @ApiResponse(responseCode = "404", description = "No se encontro el recurso"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    ResponseEntity<ResponseParadaDTO> actualizarParada(
            @PathVariable @NotNull Long id,
            @RequestBody @Valid RequestParadaDTO paradaDTO) {

        return ResponseEntity.ok(service.actualizarParada(id, paradaDTO));
    }

    @Operation(summary = "Borrar una parada", description = "Eliminado de una parada concreta")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Parada eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontro el recurso"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> borrarParada(
            @PathVariable @NotNull Long id) {
        service.eliminarParada(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener paradas por id", description = "Devuelve en una lista todas las paradas requeridas por id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Paradas obtenidas correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    //para mapear en viaje
    @PostMapping("/batch")
    ResponseEntity<List<ResponseParadaDTO>> getParadasByIds(
            @Parameter(description = "Lista de IDs de paradas a buscar")
            @RequestBody List<Long> ids) {
        return ResponseEntity.ok(service.getParadasById(ids));
    }
}
