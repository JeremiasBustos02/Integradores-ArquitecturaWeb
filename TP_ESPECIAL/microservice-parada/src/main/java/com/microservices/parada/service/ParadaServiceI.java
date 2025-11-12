package com.microservices.parada.service;

import com.microservices.parada.dto.request.RequestParadaDTO;
import com.microservices.parada.dto.response.ResponseParadaDTO;

import java.util.List;

public interface ParadaServiceI {

    ResponseParadaDTO crearParada(RequestParadaDTO requestDTO);

    ResponseParadaDTO actualizarParada(Long id, RequestParadaDTO requestDTO);

    List<ResponseParadaDTO> obtenerTodas();

    ResponseParadaDTO getParadaById(Long id);

    void eliminarParada(Long id);

    List<ResponseParadaDTO> getParadasById(List<Long> idParadas);
}

