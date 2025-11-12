package com.microservices.tarifas.repository;

import com.microservices.tarifas.entity.PrecioVigente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrecioVigenteRepository extends JpaRepository<PrecioVigente, Long> {
    
    @Query("SELECT p FROM PrecioVigente p WHERE p.activo = true ORDER BY p.vigenteDesde DESC")
    List<PrecioVigente> findActivos();
}

