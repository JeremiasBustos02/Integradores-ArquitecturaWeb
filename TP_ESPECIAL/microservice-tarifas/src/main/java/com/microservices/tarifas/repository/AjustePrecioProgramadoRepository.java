package com.microservices.tarifas.repository;

import com.microservices.tarifas.entity.AjustePrecioProgramado;
import com.microservices.tarifas.entity.EstadoAjuste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface AjustePrecioProgramadoRepository extends JpaRepository<AjustePrecioProgramado, Long> {
    
    List<AjustePrecioProgramado> findByEstadoAndEfectivaDesdeBefore(EstadoAjuste estado, OffsetDateTime fecha);
}

