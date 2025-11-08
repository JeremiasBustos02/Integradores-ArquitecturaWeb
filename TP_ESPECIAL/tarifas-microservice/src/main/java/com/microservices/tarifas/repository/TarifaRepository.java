package com.microservices.tarifas.repository;

import com.microservices.tarifas.entity.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    
    Optional<Tarifa> findByActivaTrue();
    
    List<Tarifa> findByFechaVigenciaLessThanEqualOrderByFechaVigenciaDesc(LocalDate fecha);
    
    @Query("SELECT t FROM Tarifa t WHERE t.fechaVigencia <= :fecha AND t.activa = true ORDER BY t.fechaVigencia DESC")
    Optional<Tarifa> findTarifaVigenteByFecha(LocalDate fecha);
    
    List<Tarifa> findByFechaVigenciaBetween(LocalDate fechaInicio, LocalDate fechaFin);
}

