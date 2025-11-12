package com.monopatines.admin.repository;
import com.monopatines.admin.domain.AjustePrecioProgramado; 
import com.monopatines.admin.domain.EstadoAjuste; 
import org.springframework.data.jpa.repository.JpaRepository; 
import java.time.OffsetDateTime; 
import java.util.List;

public interface AjustePrecioProgramadoRepository extends JpaRepository<AjustePrecioProgramado, Long> {
 
    List<AjustePrecioProgramado> findByEstadoAndEfectivaDesdeBefore(EstadoAjuste estado, OffsetDateTime fecha);
}