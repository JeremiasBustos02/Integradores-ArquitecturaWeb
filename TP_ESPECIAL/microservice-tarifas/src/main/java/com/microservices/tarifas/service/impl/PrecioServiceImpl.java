package com.microservices.tarifas.service.impl;

import com.microservices.tarifas.dto.request.AjustePrecioRequestDTO;
import com.microservices.tarifas.dto.request.PrecioVigenteRequestDTO;
import com.microservices.tarifas.dto.response.AjustePrecioResponseDTO;
import com.microservices.tarifas.dto.response.PrecioVigenteResponseDTO;
import com.microservices.tarifas.entity.AjustePrecioProgramado;
import com.microservices.tarifas.entity.EstadoAjuste;
import com.microservices.tarifas.entity.PrecioVigente;
import com.microservices.tarifas.repository.AjustePrecioProgramadoRepository;
import com.microservices.tarifas.repository.PrecioVigenteRepository;
import com.microservices.tarifas.service.IPrecioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PrecioServiceImpl implements IPrecioService {
    
    private final PrecioVigenteRepository precioRepository;
    private final AjustePrecioProgramadoRepository ajusteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PrecioVigenteResponseDTO> listarVigentes() {
        log.debug("Listando precios vigentes");
        return precioRepository.findActivos()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PrecioVigenteResponseDTO definirVigente(PrecioVigenteRequestDTO dto) {
        log.info("Definiendo nuevo precio vigente");
        
        PrecioVigente p = new PrecioVigente();
        p.setPrecioKm(dto.getPrecioKm());
        p.setPrecioMin(dto.getPrecioMin());
        p.setMoneda(dto.getMoneda());
        p.setVigenteDesde(dto.getVigenteDesde() != null ? dto.getVigenteDesde() : OffsetDateTime.now());
        p.setVigenteHasta(dto.getVigenteHasta());
        p.setActivo(true);
        
        PrecioVigente saved = precioRepository.save(p);
        log.info("Precio vigente creado con ID: {}", saved.getId());
        
        return toResponseDTO(saved);
    }

    @Override
    @Transactional
    public AjustePrecioResponseDTO programarAjuste(AjustePrecioRequestDTO dto) {
        log.info("Programando ajuste de precio para: {}", dto.getEfectivaDesde());
        
        AjustePrecioProgramado a = new AjustePrecioProgramado();
        a.setPrecioKm(dto.getPrecioKm());
        a.setPrecioMin(dto.getPrecioMin());
        a.setExtraPausaMultiplicador(dto.getExtraPausaMultiplicador());
        a.setEfectivaDesde(dto.getEfectivaDesde());
        a.setEstado(EstadoAjuste.PENDIENTE);
        
        AjustePrecioProgramado saved = ajusteRepository.save(a);
        log.info("Ajuste programado con ID: {}", saved.getId());
        
        return new AjustePrecioResponseDTO(
                saved.getId(),
                saved.getPrecioKm(),
                saved.getPrecioMin(),
                saved.getExtraPausaMultiplicador(),
                saved.getEfectivaDesde(),
                saved.getEstado().name()
        );
    }

    private PrecioVigenteResponseDTO toResponseDTO(PrecioVigente p) {
        return new PrecioVigenteResponseDTO(
                p.getId(),
                p.getPrecioKm(),
                p.getPrecioMin(),
                p.getMoneda(),
                p.getVigenteDesde(),
                p.getVigenteHasta(),
                p.getActivo()
        );
    }
}

