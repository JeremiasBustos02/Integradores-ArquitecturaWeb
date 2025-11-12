package com.monopatines.admin.service.impl;
import com.monopatines.admin.domain.AjustePrecioProgramado; 
import com.monopatines.admin.domain.EstadoAjuste; 
import com.monopatines.admin.domain.PrecioVigente;
import com.monopatines.admin.dto.AjustePrecioProgramadoDTO; 
import com.monopatines.admin.dto.PrecioVigenteDTO;
import com.monopatines.admin.repository.AjustePrecioProgramadoRepository; 
import com.monopatines.admin.repository.PrecioRepository; 
import com.monopatines.admin.service.PrecioService;
import lombok.RequiredArgsConstructor; 
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime; 
import java.util.List; 
import java.util.stream.Collectors;

@Service 
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PrecioServiceImpl implements PrecioService {
    private final PrecioRepository precioRepository; 
    private final AjustePrecioProgramadoRepository ajusteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PrecioVigenteDTO> listarVigentes(){ 
        log.debug("Listando precios vigentes");
        return precioRepository.findActivos()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList()); 
    }

    @Override
    @Transactional
    public PrecioVigenteDTO definirVigente(PrecioVigenteDTO dto){
        log.info("Definiendo nuevo precio vigente");
        
        PrecioVigente p = new PrecioVigente(); 
        BeanUtils.copyProperties(dto, p);
        
        if(p.getVigenteDesde() == null) {
            p.setVigenteDesde(OffsetDateTime.now());
        }
        p.setActivo(true); 
        
        PrecioVigente saved = precioRepository.save(p);
        log.info("Precio vigente creado con ID: {}", saved.getId());
        
        return toDto(saved); 
    }

    @Override
    @Transactional
    public AjustePrecioProgramadoDTO programarAjuste(AjustePrecioProgramadoDTO dto){
        log.info("Programando ajuste de precio para: {}", dto.getEfectivaDesde());
        
        AjustePrecioProgramado a = new AjustePrecioProgramado();
        a.setPrecioKm(dto.getPrecioKm()); 
        a.setPrecioMin(dto.getPrecioMin());
        a.setExtraPausaMultiplicador(dto.getExtraPausaMultiplicador()); 
        a.setEfectivaDesde(dto.getEfectivaDesde());
        a.setEstado(EstadoAjuste.PENDIENTE); 
        
        a = ajusteRepository.save(a);
        log.info("Ajuste programado con ID: {}", a.getId());
        
        dto.setId(a.getId()); 
        dto.setEstado(a.getEstado().name()); 
        
        return dto; 
    }

    private PrecioVigenteDTO toDto(PrecioVigente e){ 
        PrecioVigenteDTO d = new PrecioVigenteDTO(); 
        BeanUtils.copyProperties(e, d); 
        return d; 
    }
}