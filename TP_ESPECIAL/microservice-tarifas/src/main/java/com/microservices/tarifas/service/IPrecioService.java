package com.microservices.tarifas.service;

import com.microservices.tarifas.dto.request.AjustePrecioRequestDTO;
import com.microservices.tarifas.dto.request.PrecioVigenteRequestDTO;
import com.microservices.tarifas.dto.response.AjustePrecioResponseDTO;
import com.microservices.tarifas.dto.response.PrecioVigenteResponseDTO;

import java.util.List;

public interface IPrecioService {
    
    List<PrecioVigenteResponseDTO> listarVigentes();
    
    PrecioVigenteResponseDTO definirVigente(PrecioVigenteRequestDTO dto);
    
    AjustePrecioResponseDTO programarAjuste(AjustePrecioRequestDTO dto);
}

