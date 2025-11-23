package com.microservices.facturacion.controller;

import com.microservices.facturacion.dto.request.FacturaRequestDTO;
import com.microservices.facturacion.dto.response.FacturaResponseDTO;
import com.microservices.facturacion.dto.response.ReporteTotalFacturadoDTO;
import com.microservices.facturacion.entity.EstadoFactura;
import com.microservices.facturacion.service.IFacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
@Tag(name = "Servicio de creacion de facturas y pagos", description = "Crear facturas y validar pagos de cuentas externas")
public class FacturaController {

    private final IFacturaService facturaService;

    @Operation(summary = "Creacion de facturas", description = "Crea la factura dado un viaje concreto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "La factura se creo correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos para crear la factura"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
    })
    @PostMapping
    public ResponseEntity<FacturaResponseDTO> crearFactura(@Valid @RequestBody FacturaRequestDTO requestDTO) {
        FacturaResponseDTO factura = facturaService.crearFactura(requestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(factura.getId())
                .toUri();

        return ResponseEntity.created(location).body(factura);
    }

    @Operation(summary = "Actualizacion de facturas", description = "Modifica una factura dada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La factura se actualizo correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de actualizacion"),
            @ApiResponse(responseCode = "404", description = "Error en encontrar el recurso"),

            @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<FacturaResponseDTO> actualizarFactura(
            @PathVariable Long id,
            @Valid @RequestBody FacturaRequestDTO requestDTO) {
        FacturaResponseDTO factura = facturaService.actualizarFactura(id, requestDTO);
        return ResponseEntity.ok(factura);
    }

    @Operation(summary = "Obtener una sola factura", description = "Obtiene una factura por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La factura se obtuvo correctamente"),
            @ApiResponse(responseCode = "404", description = "Error en encontrar el recurso"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponseDTO> obtenerFacturaPorId(@PathVariable Long id) {
        FacturaResponseDTO factura = facturaService.obtenerFacturaPorId(id);
        return ResponseEntity.ok(factura);
    }

    @Operation(summary = "Obtener todas las facturas", description = "Devuelve todas las facturas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La factura se obtuvo correctamente"),
            @ApiResponse(responseCode = "404", description = "Error en encontrar el recurso"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
    })
    @GetMapping
    public ResponseEntity<List<FacturaResponseDTO>> obtenerTodasLasFacturas() {
        List<FacturaResponseDTO> facturas = facturaService.obtenerTodasLasFacturas();
        return ResponseEntity.ok(facturas);
    }

    @Operation(summary = "Eliminar una factura", description = "Borra una factura dada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "La factura se borró correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró la factura"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFactura(@PathVariable Long id) {
        facturaService.eliminarFactura(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener todas las facturas por cuenta", description = "Devuelve todas las facturas que tiene una cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Las facturas se encontraron correctamente"),
            @ApiResponse(responseCode = "404", description = "Error en encontrar el recurso"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
    })
    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<FacturaResponseDTO>> obtenerFacturasPorCuenta(@PathVariable Long cuentaId) {
        List<FacturaResponseDTO> facturas = facturaService.obtenerFacturasPorCuenta(cuentaId);
        return ResponseEntity.ok(facturas);
    }

    @Operation(summary = "Obtener facturas por estado", description = "Devuelve todas las facturas con un estado dado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Las facturas se encontraron correctamente"),
            @ApiResponse(responseCode = "404", description = "Error en encontrar el recurso"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<FacturaResponseDTO>> obtenerFacturasPorEstado(@PathVariable EstadoFactura estado) {
        List<FacturaResponseDTO> facturas = facturaService.obtenerFacturasPorEstado(estado);
        return ResponseEntity.ok(facturas);
    }

    @Operation(summary = "Facturas por fechas", description = "Devuelva las facturas dentro de dos fechas dadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Las facturas se encontraron correctamente"),
            @ApiResponse(responseCode = "404", description = "Error en encontrar el recurso"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
    })
    @GetMapping("/rango-fechas")
    public ResponseEntity<List<FacturaResponseDTO>> obtenerFacturasPorRangoFechas(
            @Parameter(description = "Fecha de inicio", example = "2")

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @Parameter(description = "Fecha de fin", example = "3")

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<FacturaResponseDTO> facturas = facturaService.obtenerFacturasPorRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(facturas);
    }

    @Operation(summary = "Facturas por cuenta y fecha", description = "Devuelva las facturas de una cuenta dentro de dos fechas dadas ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Las facturas se encontraron correctamente"),
            @ApiResponse(responseCode = "404", description = "Error en encontrar el recurso"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
    })
    @GetMapping("/cuenta/{cuentaId}/rango-fechas")
    public ResponseEntity<List<FacturaResponseDTO>> obtenerFacturasPorCuentaYRangoFechas(
            @PathVariable Long cuentaId,
            @Parameter(description = "Fecha de inicio", example = "2")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @Parameter(description = "Fecha de fin", example = "3")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<FacturaResponseDTO> facturas = facturaService.obtenerFacturasPorCuentaYRangoFechas(cuentaId, fechaInicio, fechaFin);
        return ResponseEntity.ok(facturas);
    }

    @Operation(summary = "Reporte de facturacion por meses", description = "Devuelve la cantidad de facturacion en un rango de X meses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Las facturas se encontraron correctamente"),
            @ApiResponse(responseCode = "404", description = "Error en encontrar el recurso"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
    })
    @GetMapping("/reporte/total-facturado")
    public ResponseEntity<ReporteTotalFacturadoDTO> obtenerTotalFacturadoEnRangoMeses(
            @Parameter(description = "Mes de inicio (1-12)", example = "1")
            @RequestParam Integer mesInicio,
            @Parameter(description = "Mes de fin (1-12)", example = "6")
            @RequestParam Integer mesFin,
            @Parameter(description = "Año del reporte", example = "2025")
            @RequestParam Integer anio) {
        ReporteTotalFacturadoDTO reporte = facturaService.obtenerTotalFacturadoEnRangoMeses(mesInicio, mesFin, anio);
        return ResponseEntity.ok(reporte);
    }

    @Operation(summary = "Actualizar estado", description = "Actualiza el estado de las facturas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La factura se actualizo correctamente"),
            @ApiResponse(responseCode = "404", description = "Error en encontrar el recurso"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<FacturaResponseDTO> cambiarEstadoFactura(
            @Parameter(description = "Id de la factura", example = "103")

            @PathVariable Long id,
            @Parameter(description = "Estado nuevo", example = "PAGADA")

            @RequestParam EstadoFactura estado) {
        FacturaResponseDTO factura = facturaService.cambiarEstadoFactura(id, estado);
        return ResponseEntity.ok(factura);
    }
}

