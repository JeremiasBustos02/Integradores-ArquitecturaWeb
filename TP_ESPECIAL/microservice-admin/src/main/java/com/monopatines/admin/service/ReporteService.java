package com.monopatines.admin.service;
import com.monopatines.admin.dto.*; 
import java.time.LocalDate; 
import java.util.List;

public interface ReporteService {
    List<UsoMonopatinDTO> reporteUso(LocalDate desde, LocalDate hasta, boolean incluirPausas);
    List<MonopatinResumenDTO> monopatinesConMasViajes(int anio, int minViajes);
    TotalFacturadoDTO totalFacturado(int anio, int mesDesde, int mesHasta);
    List<UsuarioUsoDTO> usuariosMasFrecuentes(LocalDate desde, LocalDate hasta, String tipo);
}