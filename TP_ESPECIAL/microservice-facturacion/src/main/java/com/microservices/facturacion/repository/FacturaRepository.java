package com.microservices.facturacion.repository;

import com.microservices.facturacion.entity.EstadoFactura;
import com.microservices.facturacion.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    List<Factura> findByCuentaId(Long cuentaId);

    List<Factura> findByEstado(EstadoFactura estado);

    List<Factura> findByFechaEmisionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<Factura> findByCuentaIdAndFechaEmisionBetween(Long cuentaId, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    @Query("SELECT SUM(f.montoTotal) FROM Factura f WHERE f.periodoMes >= :mesInicio AND f.periodoMes <= :mesFin AND f.periodoAnio = :anio AND f.estado = 'PAGADA'")
    Double calcularTotalFacturadoEnRangoMeses(@Param("mesInicio") Integer mesInicio, @Param("mesFin") Integer mesFin, @Param("anio") Integer anio);

    @Query("SELECT COUNT(f) FROM Factura f WHERE f.periodoMes >= :mesInicio AND f.periodoMes <= :mesFin AND f.periodoAnio = :anio AND f.estado = 'PAGADA'")
    Long contarFacturasEnRangoMeses(@Param("mesInicio") Integer mesInicio, @Param("mesFin") Integer mesFin, @Param("anio") Integer anio);

    List<Factura> findByPeriodoMesAndPeriodoAnio(Integer mes, Integer anio);
}