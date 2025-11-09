package com.microservices.tarifas.service.impl;

import com.microservices.tarifas.dto.request.TarifaRequestDTO;
import com.microservices.tarifas.dto.response.TarifaResponseDTO;
import com.microservices.tarifas.entity.Tarifa;
import com.microservices.tarifas.exception.TarifaNotFoundException;
import com.microservices.tarifas.mapper.TarifaMapper;
import com.microservices.tarifas.repository.TarifaRepository;
import com.microservices.tarifas.service.ITarifaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TarifaService implements ITarifaService {
    
    private final TarifaRepository tarifaRepository;
    private final TarifaMapper tarifaMapper;
    
    @Override
    @Transactional
    public TarifaResponseDTO crearTarifa(TarifaRequestDTO requestDTO) {
        Tarifa tarifa = tarifaMapper.toEntity(requestDTO);
        
        // Si la nueva tarifa es activa, desactivar todas las demás
        if (tarifa.getActiva()) {
            desactivarTodasLasTarifas();
        }
        
        Tarifa tarifaGuardada = tarifaRepository.save(tarifa);
        return tarifaMapper.toResponseDTO(tarifaGuardada);
    }
    
    @Override
    @Transactional
    public TarifaResponseDTO actualizarTarifa(Long id, TarifaRequestDTO requestDTO) {
        Tarifa tarifa = tarifaRepository.findById(id)
                .orElseThrow(() -> new TarifaNotFoundException("Tarifa no encontrada con id: " + id));
        
        tarifa.setMontoBase(requestDTO.getMontoBase());
        tarifa.setMontoExtra(requestDTO.getMontoExtra());
        tarifa.setFechaVigencia(requestDTO.getFechaVigencia());
        tarifa.setDescripcion(requestDTO.getDescripcion());
        
        // Si se activa esta tarifa, desactivar todas las demás
        if (requestDTO.getActiva() && !tarifa.getActiva()) {
            desactivarTodasLasTarifas();
        }
        
        tarifa.setActiva(requestDTO.getActiva());
        
        Tarifa tarifaActualizada = tarifaRepository.save(tarifa);
        return tarifaMapper.toResponseDTO(tarifaActualizada);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TarifaResponseDTO obtenerTarifaPorId(Long id) {
        Tarifa tarifa = tarifaRepository.findById(id)
                .orElseThrow(() -> new TarifaNotFoundException("Tarifa no encontrada con id: " + id));
        return tarifaMapper.toResponseDTO(tarifa);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TarifaResponseDTO> obtenerTodasLasTarifas() {
        return tarifaRepository.findAll()
                .stream()
                .map(tarifaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void eliminarTarifa(Long id) {
        if (!tarifaRepository.existsById(id)) {
            throw new TarifaNotFoundException("Tarifa no encontrada con id: " + id);
        }
        tarifaRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TarifaResponseDTO obtenerTarifaActiva() {
        Tarifa tarifa = tarifaRepository.findByActivaTrue()
                .orElseThrow(() -> new TarifaNotFoundException("No hay tarifa activa en el sistema"));
        return tarifaMapper.toResponseDTO(tarifa);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TarifaResponseDTO obtenerTarifaVigenteEnFecha(LocalDate fecha) {
        Tarifa tarifa = tarifaRepository.findTarifaVigenteByFecha(fecha)
                .orElseThrow(() -> new TarifaNotFoundException("No hay tarifa vigente para la fecha: " + fecha));
        return tarifaMapper.toResponseDTO(tarifa);
    }
    
    @Override
    @Transactional
    public TarifaResponseDTO ajustarPreciosDesdeFecha(LocalDate fechaVigencia, Double montoBase, Double montoExtra, String descripcion) {
        // Desactivar la tarifa actual
        desactivarTodasLasTarifas();
        
        // Crear nueva tarifa con los nuevos precios
        Tarifa nuevaTarifa = new Tarifa();
        nuevaTarifa.setMontoBase(montoBase);
        nuevaTarifa.setMontoExtra(montoExtra);
        nuevaTarifa.setFechaVigencia(fechaVigencia);
        nuevaTarifa.setActiva(true);
        nuevaTarifa.setDescripcion(descripcion);
        
        Tarifa tarifaGuardada = tarifaRepository.save(nuevaTarifa);
        return tarifaMapper.toResponseDTO(tarifaGuardada);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TarifaResponseDTO> obtenerTarifasPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return tarifaRepository.findByFechaVigenciaBetween(fechaInicio, fechaFin)
                .stream()
                .map(tarifaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    private void desactivarTodasLasTarifas() {
        List<Tarifa> tarifasActivas = tarifaRepository.findAll()
                .stream()
                .filter(Tarifa::getActiva)
                .collect(Collectors.toList());
        
        tarifasActivas.forEach(t -> t.setActiva(false));
        tarifaRepository.saveAll(tarifasActivas);
    }
}

