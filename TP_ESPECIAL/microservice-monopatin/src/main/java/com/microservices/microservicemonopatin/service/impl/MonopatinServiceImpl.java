package com.microservices.microservicemonopatin.service.impl;

import com.microservices.microservicemonopatin.dto.MonopatinDTO;
import com.microservices.microservicemonopatin.dto.MonopatinRequestDTO;
import com.microservices.microservicemonopatin.entity.Monopatin;
import com.microservices.microservicemonopatin.exception.MonopatinNotFoundException;
import com.microservices.microservicemonopatin.mapper.MonopatinMapper;
import com.microservices.microservicemonopatin.repository.MonopatinRepository;
import com.microservices.microservicemonopatin.service.MonopatinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MonopatinServiceImpl implements MonopatinService {

    private final MonopatinRepository monopatinRepository;
    private final MonopatinMapper monopatinMapper;

    @Override
    public MonopatinDTO crear(MonopatinRequestDTO dto) {
        // Implementación del método crear
        log.info("Creando monopatín: {}", dto);
        // Lógica para crear un monopatín
        if (monopatinRepository.existsById(dto.getId())) {
            log.error("El monopatín con ID {} ya existe", dto.getId());
            throw new MonopatinNotFoundException("El monopatín ya existe" + dto.getId());
        }

        Monopatin monopatin = monopatinMapper.toEntity(dto);
        Monopatin savedMonopatin = monopatinRepository.save(monopatin);
        log.info("Monopatín creado con ID: {}", savedMonopatin.getId());

        return monopatinMapper.toDTO(savedMonopatin);
    }
}
