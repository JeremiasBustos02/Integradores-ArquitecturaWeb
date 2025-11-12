package com.monopatines.admin.controller;
import com.monopatines.admin.dto.*; 
import com.monopatines.admin.service.ReporteService; 
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat; 
import org.springframework.http.ResponseEntity; 
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate; 
import java.util.List;

@RestController 
@RequestMapping("/api/admin/reportes") 
@RequiredArgsConstructor
@Validated
public class ReporteController {

  private final ReporteService service;

  @GetMapping("/uso")
  public ResponseEntity<List<UsoMonopatinDTO>> uso(
          @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate desde,
          @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate hasta,
          @RequestParam(defaultValue="false") boolean incluirPausas){
    return ResponseEntity.ok(service.reporteUso(desde,hasta,incluirPausas)); 
  }

  @GetMapping("/monopatines/mas-viajes")
  public ResponseEntity<List<MonopatinResumenDTO>> masViajes(
          @RequestParam @Positive int anio,
          @RequestParam @Min(0) int minViajes){
    return ResponseEntity.ok(service.monopatinesConMasViajes(anio,minViajes)); 
  }

  @GetMapping("/facturacion")
  public ResponseEntity<TotalFacturadoDTO> totalFacturado(
          @RequestParam @Positive int anio,
          @RequestParam @Min(1) @Max(12) int mesDesde,
          @RequestParam @Min(1) @Max(12) int mesHasta){
    return ResponseEntity.ok(service.totalFacturado(anio,mesDesde,mesHasta)); 
  }

  @GetMapping("/usuarios")
  public ResponseEntity<List<UsuarioUsoDTO>> usuariosMasFrecuentes(
          @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate desde,
          @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate hasta,
          @RequestParam(required=false) String tipo){
    return ResponseEntity.ok(service.usuariosMasFrecuentes(desde,hasta,tipo)); 
  }
}
