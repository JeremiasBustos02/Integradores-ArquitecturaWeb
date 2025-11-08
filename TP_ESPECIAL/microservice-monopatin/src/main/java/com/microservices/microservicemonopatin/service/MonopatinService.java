package com.microservices.microservicemonopatin.service;

import com.microservices.microservicemonopatin.dto.MonopatinCercanoDTO;
import com.microservices.microservicemonopatin.dto.MonopatinDTO;
import com.microservices.microservicemonopatin.dto.MonopatinRequestDTO;
import com.microservices.microservicemonopatin.dto.ReporteKilometrosDTO;
import com.microservices.microservicemonopatin.entity.EstadoMonopatin;

import java.util.List;

public interface MonopatinService {

    MonopatinDTO crear(MonopatinRequestDTO dto);
    MonopatinDTO obtenerPorId(Long id);
    List<MonopatinDTO> obtenerTodos();
    MonopatinDTO actualizar(Long id, MonopatinRequestDTO dto);
    void eliminar(Long id);

    MonopatinDTO marcarEnMantenimiento(Long id);
    MonopatinDTO sacarDeMantenimiento(Long id);

    MonopatinDTO cambiarEstado(Long id, EstadoMonopatin nuevoEstado);

    List<MonopatinCercanoDTO> buscarCercanos(Double latitud, Double longitud, Double radioKm);

    List<ReporteKilometrosDTO> reporteKilometros(boolean incluirPausas);

    List<MonopatinDTO> obtenerConMasDeXViajes(int cantidadMinima);

    void actualizarUbicacion(Long id, Double latitud, Double longitud);

    void registrarUsoDeViaje(Long id, Double kmRecorridos, Long tiempoUso, Long tiempoPausas);
    
}
