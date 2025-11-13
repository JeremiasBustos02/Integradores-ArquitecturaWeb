package com.microservices.parada.service.impl;

import com.microservices.parada.dto.request.RequestParadaDTO;
import com.microservices.parada.mapper.ParadaMapper;
import com.microservices.parada.dto.response.ResponseParadaDTO;
import com.microservices.parada.entity.Parada;
import com.microservices.parada.repository.ParadaRepository;
import com.microservices.parada.service.ParadaServiceI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ParadaService implements ParadaServiceI {
    private final ParadaRepository paradaRepository;
    private final ParadaMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ResponseParadaDTO> getParadasById(List<Long> idParadas) {
        if (idParadas == null || idParadas.isEmpty()) {
            return new ArrayList<>();
        }
        log.info("Buscando {} paradas por lote", idParadas.size());
        List<Parada> paradas = paradaRepository.findAllByIdIn(idParadas);
        return mapper.toResponseDTOList(paradas);
    }

    @Override
    public ResponseParadaDTO crearParada(RequestParadaDTO requestDTO) {
        Parada parada = mapper.toEntity(requestDTO);
        Parada paradaGuardada = paradaRepository.save(parada);
        log.info("Parada creada con ID: {}", paradaGuardada.getId());
        return mapper.toResponseDTO(paradaGuardada);
    }

    @Override
    public ResponseParadaDTO actualizarParada(Long id, RequestParadaDTO requestDTO) {
        Parada paradaExistente = paradaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontro la parada con id " + id));

        paradaExistente.setNombre(requestDTO.getNombre());
        paradaExistente.setLatitud(requestDTO.getLatitud());
        paradaExistente.setLongitud(requestDTO.getLongitud());

        Parada paradaActualizada = paradaRepository.save(paradaExistente);
        log.info("Parada actualizada con ID: {}", paradaActualizada.getId());
        return mapper.toResponseDTO(paradaActualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseParadaDTO> obtenerTodas() {
        return mapper.toResponseDTOList(paradaRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseParadaDTO getParadaById(Long id) {
        if (id == null || id < 0) throw new IllegalArgumentException("El id debe ser valido");
        Parada parada = paradaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontro la parada con id " + id));
        return mapper.toResponseDTO(parada);
    }

    @Override
    public void eliminarParada(Long id) {
        if (!paradaRepository.existsById(id)) {
            throw new IllegalArgumentException("No se encontro la parada con id " + id);
        }
        paradaRepository.deleteById(id);
        log.info("Parada eliminada con ID: {}", id);
    }
}
