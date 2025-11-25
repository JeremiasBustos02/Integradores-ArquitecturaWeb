package com.microservices.microservicemonopatin.controller;

import com.microservices.microservicemonopatin.dto.MonopatinCercanoDTO;
import com.microservices.microservicemonopatin.dto.MonopatinDTO;
import com.microservices.microservicemonopatin.dto.MonopatinRequestDTO;
import com.microservices.microservicemonopatin.dto.ReporteKilometrosDTO;
import com.microservices.microservicemonopatin.entity.EstadoMonopatin;
import com.microservices.microservicemonopatin.service.MonopatinService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Servicio de monopatin", description = "Creacion y actualizacion de estados de monopatines")
public class MonopatinController {

    private final MonopatinService monopatinService;

    // ==================== CRUD BÁSICO ====================
    @Operation(summary = "Crear monopatines", description = "Creacion de monopatines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Creacion de monopatin correcta"),
            @ApiResponse(responseCode = "400", description = "Error en la creacion del monopatin"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),

    })
    @PostMapping
    public ResponseEntity<MonopatinDTO> crear(
            @Valid @RequestBody MonopatinRequestDTO monopatinDTO) {
        MonopatinDTO creado = monopatinService.crear(monopatinDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();

        return ResponseEntity.created(location).body(creado);
    }

    @Operation(summary = "Obtener monopatin por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obtencion de monopatin correcta"),
            @ApiResponse(responseCode = "404", description = "Error al obtener el recurso"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),

    })
    @GetMapping("/{id}")
    public ResponseEntity<MonopatinDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(monopatinService.obtenerPorId(id));
    }

    @Operation(summary = "Obtener todos los monopatines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obtencion de monopatines correcta"),
            @ApiResponse(responseCode = "404", description = "Error al obtener los recursos"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),

    })
    @GetMapping
    public ResponseEntity<List<MonopatinDTO>> obtenerTodos() {
        return ResponseEntity.ok(monopatinService.obtenerTodos());
    }

    @Operation(summary = "Actualizar monopatin", description = "Actualiza un monopatin por id ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monopatin actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Error al obtener el recurso"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),

    })
    @PutMapping("/{id}")
    public ResponseEntity<MonopatinDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MonopatinRequestDTO monopatinDTO) {
        return ResponseEntity.ok(monopatinService.actualizar(id, monopatinDTO));
    }

    @Operation(summary = "Borrar un monopatin", description = "Borrado de monopatin por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Borrado de monopatin correcto"),
            @ApiResponse(responseCode = "404", description = "Error al obtener el recurso"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),

    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        monopatinService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== GESTIÓN DE MANTENIMIENTO ====================
    @Operation(summary = "Mantenimiento de monopatin", description = "Se marca el monopatin como en mantenimiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monopatin actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Error al obtener el recurso"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),

    })
    @PutMapping("/{id}/mantenimiento")
    public ResponseEntity<MonopatinDTO> marcarEnMantenimiento(@PathVariable Long id) {
        return ResponseEntity.ok(monopatinService.marcarEnMantenimiento(id));
    }

    @Operation(summary = "Fin de mantenimiento", description = "Finalizar el mantenimiento de un monopatin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mantenimiento de monopatin terminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Error al obtener el recurso"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),

    })
    @PutMapping("/{id}/mantenimiento/finalizar")
    public ResponseEntity<MonopatinDTO> sacarDeMantenimiento(@PathVariable Long id) {
        return ResponseEntity.ok(monopatinService.sacarDeMantenimiento(id));
    }

    // ==================== GESTIÓN DE ESTADOS ====================
    @Operation(summary = "Cambiar estado de monopatin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado de monopatin cambiado correctamente"),
            @ApiResponse(responseCode = "404", description = "Error al obtener el recurso"),
            @ApiResponse(responseCode = "400", description = "Error al cambiar el recurso"),
            @ApiResponse(responseCode = "409", description = "No se puede cambiar al estado solicitado"),

            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),

    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<MonopatinDTO> cambiarEstado(
            @PathVariable Long id,
            @Parameter(description = "Estado nuevo del monopatin", example = "EN_USO")
            @RequestParam EstadoMonopatin estado) {
        return ResponseEntity.ok(monopatinService.cambiarEstado(id, estado));
    }

    // ==================== BÚSQUEDA Y UBICACIÓN ====================

    /**
     * g. Como usuario quiero buscar un listado de los monopatines cercanos a mi zona
     */
    @Operation(summary = "Listado de monopatines cercanos", description = "Obtiene todos los monopatines cercanos a una posicion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obtencion de monopatin correcta"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),

    })
    @GetMapping("/cercanos")
    public ResponseEntity<List<MonopatinCercanoDTO>> buscarCercanos(
            @Parameter(description = "Latitud actual")
            @RequestParam Double latitud,
            @Parameter(description = "Longitud actual")
            @RequestParam Double longitud,
            @Parameter(description = "Radio de busqueda")
            @RequestParam(defaultValue = "5.0") Double radioKm) {
        return ResponseEntity.ok(monopatinService.buscarCercanos(latitud, longitud, radioKm));
    }

    @Operation(summary = "Actualizar ubicacion", description = "Actualiza la ubicacion de un monopatin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Actualizacion de posicion de monopatin correcta"),
            @ApiResponse(responseCode = "404", description = "Error al obtener el recurso"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),

    })
    @PutMapping("/{id}/ubicacion")
    public ResponseEntity<Void> actualizarUbicacion(
            @PathVariable Long id,
            @Parameter(description = "Latitud actual")

            @RequestParam Double latitud,
            @Parameter(description = "Longitud actual")

            @RequestParam Double longitud) {
        monopatinService.actualizarUbicacion(id, latitud, longitud);
        return ResponseEntity.noContent().build();
    }

    // ==================== REPORTES ====================
    @Operation(summary = "Obtener reporte de monopatines por kilometros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obtencion de monopatines correcta"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),

    })
    /**
     * a. Como administrador quiero poder generar un reporte de uso de monopatines por kilómetros
     */
    @GetMapping("/reporte/kilometros")
    public ResponseEntity<List<ReporteKilometrosDTO>> reporteKilometros(
            @Parameter(description = "Incluir pausas de monopatin en el reporte")

            @RequestParam(defaultValue = "false") boolean incluirPausas) {
        return ResponseEntity.ok(monopatinService.reporteKilometros(incluirPausas));
    }

    @Operation(summary = "Obtener reporte de monopatines por viajes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obtencion de monopatines correcta"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),

    })
    /**
     * c. Como administrador quiero consultar los monopatines con más de X viajes en un cierto año
     */
    @GetMapping("/reporte/viajes")
    public ResponseEntity<List<MonopatinDTO>> obtenerConMasDeXViajes(
            @Parameter(description = "Obtener monopatin con x cantidad de viajes")
            @RequestParam int cantidad) {
        return ResponseEntity.ok(monopatinService.obtenerConMasDeXViajes(cantidad));
    }

    // ==================== USO Y VIAJES ====================

    /**
     * Endpoint interno para que el microservicio de viajes actualice el uso del monopatín
     */
    @Hidden
    @Operation(summary = "Registrar el viaje")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Viaje registrado"),
            @ApiResponse(responseCode = "404", description = "Error al obtener el recurso"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),

    })
    @PutMapping("/{id}/registrar-uso")
    public ResponseEntity<Void> registrarUsoDeViaje(
            @PathVariable Long id,
            @Parameter(description = "Kilometros del viaje en el monopatin")

            @RequestParam Double kmRecorridos,
            @Parameter(description = "Cantidad de tiempo total que se uso el monopatin")

            @RequestParam Long tiempoUso,
            @Parameter(description = "Cantidad total del tiempo de las pausas")

            @RequestParam Long tiempoPausas) {
        monopatinService.registrarUsoDeViaje(id, kmRecorridos, tiempoUso, tiempoPausas);
        return ResponseEntity.noContent().build();
    }
}