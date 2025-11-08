package com.microservices.facturacion.controller;

import com.microservices.facturacion.dto.request.FacturaRequestDTO;
import com.microservices.facturacion.dto.response.FacturaResponseDTO;
import com.microservices.facturacion.dto.response.ReporteTotalFacturadoDTO;
import com.microservices.facturacion.entity.EstadoFactura;
import com.microservices.facturacion.service.IFacturaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
public class FacturaController {
    
    private final IFacturaService facturaService;
    
    @PostMapping
    public ResponseEntity<FacturaResponseDTO> crearFactura(@Valid @RequestBody FacturaRequestDTO requestDTO) {
        FacturaResponseDTO factura = facturaService.crearFactura(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(factura);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<FacturaResponseDTO> actualizarFactura(
            @PathVariable Long id,
            @Valid @RequestBody FacturaRequestDTO requestDTO) {
        FacturaResponseDTO factura = facturaService.actualizarFactura(id, requestDTO);
        return ResponseEntity.ok(factura);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponseDTO> obtenerFacturaPorId(@PathVariable Long id) {
        FacturaResponseDTO factura = facturaService.obtenerFacturaPorId(id);
        return ResponseEntity.ok(factura);
    }
    
    @GetMapping
    public ResponseEntity<List<FacturaResponseDTO>> obtenerTodasLasFacturas() {
        List<FacturaResponseDTO> facturas = facturaService.obtenerTodasLasFacturas();
        return ResponseEntity.ok(facturas);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFactura(@PathVariable Long id) {
        facturaService.eliminarFactura(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<FacturaResponseDTO>> obtenerFacturasPorCuenta(@PathVariable Long cuentaId) {
        List<FacturaResponseDTO> facturas = facturaService.obtenerFacturasPorCuenta(cuentaId);
        return ResponseEntity.ok(facturas);
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<FacturaResponseDTO>> obtenerFacturasPorEstado(@PathVariable EstadoFactura estado) {
        List<FacturaResponseDTO> facturas = facturaService.obtenerFacturasPorEstado(estado);
        return ResponseEntity.ok(facturas);
    }
    
    @GetMapping("/rango-fechas")
    public ResponseEntity<List<FacturaResponseDTO>> obtenerFacturasPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<FacturaResponseDTO> facturas = facturaService.obtenerFacturasPorRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(facturas);
    }
    
    @GetMapping("/cuenta/{cuentaId}/rango-fechas")
    public ResponseEntity<List<FacturaResponseDTO>> obtenerFacturasPorCuentaYRangoFechas(
            @PathVariable Long cuentaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<FacturaResponseDTO> facturas = facturaService.obtenerFacturasPorCuentaYRangoFechas(cuentaId, fechaInicio, fechaFin);
        return ResponseEntity.ok(facturas);
    }
    
    @GetMapping("/reporte/total-facturado")
    public ResponseEntity<ReporteTotalFacturadoDTO> obtenerTotalFacturadoEnRangoMeses(
            @RequestParam Integer mesInicio,
            @RequestParam Integer mesFin,
            @RequestParam Integer anio) {
        ReporteTotalFacturadoDTO reporte = facturaService.obtenerTotalFacturadoEnRangoMeses(mesInicio, mesFin, anio);
        return ResponseEntity.ok(reporte);
    }
    
    @PatchMapping("/{id}/estado")
    public ResponseEntity<FacturaResponseDTO> cambiarEstadoFactura(
            @PathVariable Long id,
            @RequestParam EstadoFactura estado) {
        FacturaResponseDTO factura = facturaService.cambiarEstadoFactura(id, estado);
        return ResponseEntity.ok(factura);
    }
}

