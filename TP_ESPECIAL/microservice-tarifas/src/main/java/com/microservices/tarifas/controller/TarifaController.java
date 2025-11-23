package com.microservices.tarifas.controller;

import com.microservices.tarifas.dto.request.AjustePrecioRequestDTO;
import com.microservices.tarifas.dto.request.PrecioVigenteRequestDTO;
import com.microservices.tarifas.dto.request.TarifaRequestDTO;
import com.microservices.tarifas.dto.response.AjustePrecioResponseDTO;
import com.microservices.tarifas.dto.response.PrecioVigenteResponseDTO;
import com.microservices.tarifas.dto.response.TarifaResponseDTO;
import com.microservices.tarifas.service.IPrecioService;
import com.microservices.tarifas.service.ITarifaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Servicio de tarifa", description = "Crear tarifas y actualizar precios")
public class TarifaController {

    private final ITarifaService tarifaService;
    private final IPrecioService precioService;

    @Operation(summary = "Crear tarifa", description = "Creacion de tarifas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarifa creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de creacion para la tarifa"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),


    })
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

    @Operation(summary = "Actualizar tarifa", description = "Actualizacion de una tarifa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarifa actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de creacion para la tarifa"),

            @ApiResponse(responseCode = "404", description = "No se encontro el recurso"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),


    })
    @PutMapping("/{id}")
    public ResponseEntity<TarifaResponseDTO> actualizarTarifa(
            @PathVariable Long id,
            @Valid @RequestBody TarifaRequestDTO requestDTO) {
        TarifaResponseDTO tarifa = tarifaService.actualizarTarifa(id, requestDTO);
        return ResponseEntity.ok(tarifa);
    }

    @Operation(summary = "Obtener una tarifa", description = "Obtener una tarifa por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarifa obtenida correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontro el recurso"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),


    })
    @GetMapping("/{id}")
    public ResponseEntity<TarifaResponseDTO> obtenerTarifaPorId(@PathVariable Long id) {
        TarifaResponseDTO tarifa = tarifaService.obtenerTarifaPorId(id);
        return ResponseEntity.ok(tarifa);
    }

    @Operation(summary = "Obtener tarifas", description = "Obtener todas las tarifas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarifas obtenidas correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),


    })
    @GetMapping
    public ResponseEntity<List<TarifaResponseDTO>> obtenerTodasLasTarifas() {
        List<TarifaResponseDTO> tarifas = tarifaService.obtenerTodasLasTarifas();
        return ResponseEntity.ok(tarifas);
    }

    @Operation(summary = "Borrar una tarifa", description = "Eliminacion de una tarifa por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarifa eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró la tarifa"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),


    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarifa(@PathVariable Long id) {
        tarifaService.eliminarTarifa(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener tarifa activa", description = "Obtener una tarifa por estado de activa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarifa obtenida correctamente"),
            @ApiResponse(responseCode = "404", description = "No hay ninguna tarifa activa"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),


    })
    @GetMapping("/activa")
    public ResponseEntity<TarifaResponseDTO> obtenerTarifaActiva() {
        TarifaResponseDTO tarifa = tarifaService.obtenerTarifaActiva();
        return ResponseEntity.ok(tarifa);
    }

    @Operation(summary = "Obtener tarifa vigente", description = "Obtener una tarifa que esta vigente en uan fecha dada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarifa obtenida correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró tarifa para esa fecha"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),


    })
    @GetMapping("/vigente")
    public ResponseEntity<TarifaResponseDTO> obtenerTarifaVigenteEnFecha(
            @Parameter(description = "Fecha para buscar tarifas en vigencia")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        TarifaResponseDTO tarifa = tarifaService.obtenerTarifaVigenteEnFecha(fecha);
        return ResponseEntity.ok(tarifa);
    }

    @Operation(summary = "Obtener tarifas por fecha", description = "Obtener una tarifa dado dos fechas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarifas obtenidas correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),


    })
    @GetMapping("/buscar")
    public ResponseEntity<List<TarifaResponseDTO>> obtenerTarifasPorRangoFechas(
            @Parameter(description = "Fecha de inicio (YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @Parameter(description = "Fecha de fin (YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<TarifaResponseDTO> tarifas = tarifaService.obtenerTarifasPorRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(tarifas);
    }

    // ==================== GESTIÓN DE PRECIOS VIGENTES ====================

    /**
     * f. Como administrador quiero definir precios
     */
    @Operation(summary = "Obtener precios", description = "Obtener los precios que estan vigentes en la tarifa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Precios obtenidos correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),
    })
    @GetMapping("/precios")
    public ResponseEntity<List<PrecioVigenteResponseDTO>> listarPreciosVigentes() {
        log.info("REST request to get all precios vigentes");
        return ResponseEntity.ok(precioService.listarVigentes());
    }

    @Operation(summary = "Definir precios", description = "Definir precios para las tarifas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Precio cambiado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),


    })
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

    @Operation(summary = "Ajustar precios", description = "Ajuste de precios vigentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Precio cambiado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (ej: fecha no futura)"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),


    })
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

