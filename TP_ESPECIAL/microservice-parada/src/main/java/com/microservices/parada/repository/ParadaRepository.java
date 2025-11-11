package com.microservices.parada.repository;

import com.microservices.parada.entity.Parada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParadaRepository extends JpaRepository<Parada, Long> {
    List<Parada> findAllByIdIn(List<Long> ids);

}
