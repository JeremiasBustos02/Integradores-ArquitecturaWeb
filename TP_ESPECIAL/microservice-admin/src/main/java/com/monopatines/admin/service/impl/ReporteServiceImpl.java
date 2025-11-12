package com.monopatines.admin.service.impl;
import com.monopatines.admin.client.FacturacionClient; 
import com.monopatines.admin.client.MonopatinClient; 
import com.monopatines.admin.client.UserClient;
import com.monopatines.admin.dto.*; 
import com.monopatines.admin.service.ReporteService; 
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate; 
import java.util.List;

@Service 
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {
    private final MonopatinClient monopatinClient; 
    private final FacturacionClient facturacionClient; 
    private final UserClient userClient;

    public List<UsoMonopatinDTO> reporteUso(LocalDate desde, LocalDate hasta, boolean incluirPausas){ 
        return monopatinClient.reporteUso(desde,hasta,incluirPausas); 
    }

    public List<MonopatinResumenDTO> monopatinesConMasViajes(int anio, int minViajes){ 
        return monopatinClient.monopatinesConMasViajes(anio,minViajes); 
    }
 
    public TotalFacturadoDTO totalFacturado(int anio, int mesDesde, int mesHasta){ 
        return facturacionClient.total(anio,mesDesde,mesHasta); 
    }
 
    public List<UsuarioUsoDTO> usuariosMasFrecuentes(LocalDate d, LocalDate h, String t){ 
        return userClient.usuariosMasFrecuentes(d,h,t); 
    }
}