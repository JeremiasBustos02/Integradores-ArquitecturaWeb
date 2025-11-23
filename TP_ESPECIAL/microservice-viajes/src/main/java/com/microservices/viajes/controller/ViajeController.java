package com.microservices.viajes.controller;

import com.microservices.viajes.dto.request.FinalizarViajeRequestDTO;
import com.microservices.viajes.dto.request.ViajeRequestDTO;
import com.microservices.viajes.dto.response.ReporteUsoMonopatinDTO;
import com.microservices.viajes.dto.response.ViajeResponseDTO;
import com.microservices.viajes.entity.Viaje;
import com.microservices.viajes.service.ViajeServiceI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/api/viajes")
@Tag(name = "Servicio de creacion y gestion de viajes", description = "Creacion, finalizacion y eliminacion de viajes")
public class ViajeController {
    private final ViajeServiceI viajeService;

    @Operation(summary = "Crear viaje", description = "Crear un viaje")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Viaje creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos introducidos")
            , @ApiResponse(responseCode = "500", description = "Error interno en el servidor")
    })
    @PostMapping
    public ResponseEntity<ViajeResponseDTO> createViaje(@RequestBody @Valid ViajeRequestDTO request) {
        log.info("REST Request to create viaje con parada en: {}", request.getParadaInicioId());

        ViajeResponseDTO viaje = viajeService.createViaje(
                request.getMonopatinId(), request.getUsuarioId(), request.getTarifaId(), request.getParadaInicioId()
        );
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(viaje.getId())
                .toUri();

        return ResponseEntity.created(location).body(viaje);
    }

    @Operation(summary = "Finalizar viaje", description = "Finalizacion de un viaje")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Viaje finalizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos introducidos"),
            @ApiResponse(responseCode = "404", description = "No se encontro el viaje")
            , @ApiResponse(responseCode = "500", description = "Error interno en el servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ViajeResponseDTO> finalizarViaje(
            @Parameter(description = "ID del viaje a finalizar")
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

    @Operation(summary = "Iniciar pausa", description = "Creacion de una pausa en el viaje")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pausa creada correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontro el viaje")
            , @ApiResponse(responseCode = "500", description = "Error interno en el servidor")
    })
    @PostMapping("/{id}/pausa/iniciar")
    public ResponseEntity<ViajeResponseDTO> iniciarPausa(
            @Parameter(description = "ID del viaje a pausar")
            @PathVariable @NotNull String id) {
        return ResponseEntity.ok(viajeService.iniciarPausa(id));
    }

    @Operation(summary = "Finalizar pausa", description = "Finalizacion de una pausa en el viaje")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pausa finalizada correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontro el viaje")
            , @ApiResponse(responseCode = "500", description = "Error interno en el servidor")
    })
    @PutMapping("/{id}/pausa/finalizar")
    public ResponseEntity<ViajeResponseDTO> finalizarPausa(
            @Parameter(description = "ID del viaje a reanudar")
            @PathVariable @NotNull String id) {
        return ResponseEntity.ok(viajeService.finalizarPausa(id));
    }

    //PUNTO 4 A reporte de uso de monopatines por kilómetros
    //Responsabilidad de msvc-monopatin ?
    @Operation(summary = "Reporte por monopatin", description = "Creacion de reporte por monopatin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontro el monopatin")
            , @ApiResponse(responseCode = "500", description = "Error interno en el servidor")
    })
    @GetMapping("/reportes/monopatin/{monopatinid}")
    public ResponseEntity<ReporteUsoMonopatinDTO> getReportePorMonopatin(
            @Parameter(description = "ID del monopatín")
            @PathVariable @NotNull Long monopatinid,
            @Parameter(description = "Incluir pausa en el reporte")
            @RequestParam(defaultValue = "true") boolean incluirPausas) {
        return ResponseEntity.ok(viajeService.getReporteUsoMonopatin(monopatinid, incluirPausas));
    }

    //c. Como administrador quiero consultar los monopatines con más de X viajes en un cierto año.
    @Operation(summary = "Reporte por viajes", description = "Creacion de reporte de viajes por monopatin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontro el monopatin")
            , @ApiResponse(responseCode = "500", description = "Error interno en el servidor")
    })
    @GetMapping("/reportes/{monopatinid}/conteo-anual")
    public ResponseEntity<Long> getViajesAnualesPorMonopatin(
            @Parameter(description = "ID del monopatín")
            @PathVariable @NotNull Long monopatinid,
            @Parameter(description = "Año en que basar el reporte") @RequestParam int anio) {
        return ResponseEntity.ok(viajeService.contarViajesPorMonopatinEnAnio(monopatinid, anio));
    }

    @Operation(summary = "Listar viajes por usuario", description = "Obtiene todos los viajes que ha hecho un usuario dado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de usuario obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontro el usuario")
            , @ApiResponse(responseCode = "500", description = "Error interno en el servidor")
    })
    @GetMapping("/reportes/usuario/{usuarioId}")
    public ResponseEntity<List<ViajeResponseDTO>> getViajesPorUsuario(
            @Parameter(description = "ID del usuario")
            @PathVariable @NotNull Long usuarioId) {
        return ResponseEntity.ok(viajeService.getViajesByUsuario(usuarioId));
    }

    @Operation(summary = "Total viajes por monopatín", description = "Obtiene la cantidad de viajes de un monopatin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cantidad de viajes obtenida correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontro el monopatin")
            , @ApiResponse(responseCode = "500", description = "Error interno en el servidor")
    })
    @GetMapping("/reportes/{monopatinId}/cantidad")
    public ResponseEntity<Integer> getCantidadViajesPorMonopatin(
            @Parameter(description = "ID del monopatín")
            @PathVariable @NotNull Long monopatinId) {
        return ResponseEntity.ok(viajeService.viajesXMonopatin(monopatinId));
    }

    //* Como usuario quiero saber cuánto he usado los monopatines en un período...
    @Operation(summary = "Viajes por fecha", description = "Obtiene los viajes hecho por un usuario en un monopatin en dos fechas dadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Viajes obtenidos correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontro el usuario")
            , @ApiResponse(responseCode = "500", description = "Error interno en el servidor")
    })
    @GetMapping("/reportes/usuario/{usuarioId}/periodo")
    public ResponseEntity<List<ViajeResponseDTO>> getViajesPorUsuarioEnPeriodo(
            @Parameter(description = "ID del usuario")
            @PathVariable @NotNull @Positive Long usuarioId,
            @Parameter(description = "Fecha de inicio", example = "2")

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @Parameter(description = "Fecha de fin", example = "3")

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(viajeService.getViajesByUsuarioEnPeriodo(usuarioId, inicio, fin));
    }
}
