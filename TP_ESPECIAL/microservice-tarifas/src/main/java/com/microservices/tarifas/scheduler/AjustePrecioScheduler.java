package com.microservices.tarifas.scheduler;

import com.microservices.tarifas.entity.AjustePrecioProgramado;
import com.microservices.tarifas.entity.EstadoAjuste;
import com.microservices.tarifas.entity.PrecioVigente;
import com.microservices.tarifas.repository.AjustePrecioProgramadoRepository;
import com.microservices.tarifas.repository.PrecioVigenteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AjustePrecioScheduler {
    
    private final AjustePrecioProgramadoRepository ajusteRepo;
    private final PrecioVigenteRepository precioRepo;

    @Scheduled(fixedDelay = 60000) // Cada 60 segundos
    @Transactional
    public void aplicarAjustesPendientes() {
        List<AjustePrecioProgramado> pendientes = ajusteRepo.findByEstadoAndEfectivaDesdeBefore(
                EstadoAjuste.PENDIENTE, 
                OffsetDateTime.now()
        );
        
        for (AjustePrecioProgramado ajuste : pendientes) {
            log.info("Aplicando ajuste de precio id={} desde {}", ajuste.getId(), ajuste.getEfectivaDesde());
            
            PrecioVigente p = new PrecioVigente();
            p.setPrecioKm(ajuste.getPrecioKm());
            p.setPrecioMin(ajuste.getPrecioMin());
            p.setMoneda("ARS");
            p.setVigenteDesde(OffsetDateTime.now());
            p.setVigenteHasta(null);
            p.setActivo(true);
            
            precioRepo.save(p);
            
            ajuste.setEstado(EstadoAjuste.APLICADO);
            ajusteRepo.save(ajuste);
            
            log.info("Ajuste de precio {} aplicado exitosamente", ajuste.getId());
        }
    }
}

