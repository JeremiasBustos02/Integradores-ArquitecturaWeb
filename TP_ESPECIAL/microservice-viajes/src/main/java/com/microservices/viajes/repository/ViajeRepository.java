package com.microservices.viajes.repository;

import com.microservices.viajes.entity.Viaje;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ViajeRepository extends MongoRepository<Viaje, String> {
    //reporte por monopatin
    Optional<Viaje> getViajeByMonopatinId(Long id);

    List<Viaje> getViajesByMonopatinId(Long id);

    public abstract Integer countByMonopatinId(Long monopatinId);

    //reporte de uso por usuario
    List<Viaje> findByUsuarioId(Long usuarioId);


    //filtrar por fechas
    List<Viaje> findByHoraInicio(LocalDateTime inicio, LocalDateTime fin);

    // contar viajes de un monopatín en un año
    Long countByMonopatinId(Long monopatinId, LocalDateTime inicio, LocalDateTime fin);

    List<Viaje> findByUsuarioIdAndHoraInicioBetween(Long usuarioId, LocalDateTime inicio, LocalDateTime fin);

    Long countByMonopatinIdAndHoraInicioBetween(Long monopatinId, LocalDateTime anio, LocalDateTime finAnio);
}
