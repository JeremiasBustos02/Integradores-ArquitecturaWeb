package com.microservices.microservicemonopatin.repository;

import com.microservices.microservicemonopatin.entity.EstadoMonopatin;
import com.microservices.microservicemonopatin.entity.Monopatin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MonopatinRepository extends JpaRepository<Monopatin, Long> {

    List<Monopatin> findByEstado(EstadoMonopatin estado);

    List<Monopatin> findByEnMantenimiento(boolean enMantenimiento);

    // Para buscar monopatines cercanos
    @Query("SELECT m " +
            "FROM Monopatin m " +
            "WHERE m.estado = 'DISPONIBLE' " +
            "AND m.enMantenimiento = false ")
    List<Monopatin> findDisponibles();

    // Para el reporte de km
    @Query("SELECT m FROM Monopatin m ORDER BY m.kmTotales DESC")
    List<Monopatin> findAllOrderByKmTotales();

    // Comprueba si existe uno ya con ese id
    @Query("SELECT m" +
            "FROM Monopatin m " +
            "WHERE m.id = :monopatinId")
    boolean existsById(Long monopatinId);
}
