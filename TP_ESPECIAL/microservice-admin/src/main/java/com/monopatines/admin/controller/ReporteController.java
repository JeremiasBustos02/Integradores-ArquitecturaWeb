package com.monopatines.admin.controller;
import com.monopatines.admin.dto.*; 
import com.monopatines.admin.service.ReporteService; 
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate; 
import java.util.List;

@RestController 
@RequestMapping("/admin/reportes") 
@RequiredArgsConstructor
public class ReporteController {

  private final ReporteService service;
  @GetMapping("/uso")
  public ResponseEntity<List<UsoMonopatinDTO>> uso(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate desde,
                                                  @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate hasta,
                                                  @RequestParam(defaultValue="false") boolean incluirPausas){
    return ResponseEntity.ok(service.reporteUso(desde,hasta,incluirPausas)); 
  }
  @GetMapping("/monopatines/mas-viajes")
  public ResponseEntity<List<MonopatinResumenDTO>> masViajes(@RequestParam int anio, @RequestParam int minViajes){
    return ResponseEntity.ok(service.monopatinesConMasViajes(anio,minViajes)); 
  }
  @GetMapping("/facturacion")
  public ResponseEntity<TotalFacturadoDTO> totalFacturado(@RequestParam int anio, @RequestParam int mesDesde, @RequestParam int mesHasta){
    return ResponseEntity.ok(service.totalFacturado(anio,mesDesde,mesHasta)); 
  }
  @GetMapping("/usuarios")
  public ResponseEntity<List<UsuarioUsoDTO>> usuariosMasFrecuentes(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate desde,
                                                                    @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate hasta,
                                                                    @RequestParam(required=false) String tipo){
    return ResponseEntity.ok(service.usuariosMasFrecuentes(desde,hasta,tipo)); 
  }
}
