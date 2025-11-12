package com.monopatines.admin.service;
import com.monopatines.admin.dto.AjustePrecioProgramadoDTO; 
import com.monopatines.admin.dto.PrecioVigenteDTO; 
import java.util.List;

public interface PrecioService {
    List<PrecioVigenteDTO> listarVigentes();
    PrecioVigenteDTO definirVigente(PrecioVigenteDTO dto);
    AjustePrecioProgramadoDTO programarAjuste(AjustePrecioProgramadoDTO dto);
}