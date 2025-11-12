package com.monopatines.admin.scheduler;
import com.monopatines.admin.domain.AjustePrecioProgramado; 
import com.monopatines.admin.domain.EstadoAjuste; 
import com.monopatines.admin.domain.PrecioVigente;
import com.monopatines.admin.repository.AjustePrecioProgramadoRepository; 
import com.monopatines.admin.repository.PrecioRepository;
import lombok.RequiredArgsConstructor; 
import lombok.extern.slf4j.Slf4j; 
import org.springframework.scheduling.annotation.Scheduled; 
import org.springframework.stereotype.Component;
import java.time.OffsetDateTime; 
import java.util.List;

@Slf4j 
@Component 
@RequiredArgsConstructor
public class AjusteScheduler {
    private final AjustePrecioProgramadoRepository ajusteRepo; 
    private final PrecioRepository precioRepo;

    @Scheduled(fixedDelay=60000)
    public void aplicarAjustesPendientes(){
    List<AjustePrecioProgramado> pendientes = ajusteRepo.findByEstadoAndEfectivaDesdeBefore(EstadoAjuste.PENDIENTE, OffsetDateTime.now());
    
        for(AjustePrecioProgramado a: pendientes){
            log.info("Aplicando ajuste id={} desde {}", a.getId(), a.getEfectivaDesde());
        
            PrecioVigente p = new PrecioVigente(); 
            p.setPrecioKm(a.getPrecioKm()); 
            p.setPrecioMin(a.getPrecioMin());
            p.setMoneda("ARS"); 
            p.setVigenteDesde(OffsetDateTime.now()); 
            p.setActivo(true); 
            precioRepo.save(p);
            a.setEstado(EstadoAjuste.APLICADO); 
            ajusteRepo.save(a);
        }
    }
}