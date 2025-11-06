package com.microservices.usuarios.repository;

import com.microservices.usuarios.entity.Cuenta;
import com.microservices.usuarios.entity.TipoCuenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    
    Optional<Cuenta> findByIdCuentaMercadoPago(Long idCuentaMercadoPago);
    
    boolean existsByIdCuentaMercadoPago(Long idCuentaMercadoPago);
    
    Page<Cuenta> findByEstadoCuenta(Boolean estadoCuenta, Pageable pageable);
    
    Page<Cuenta> findByTipoCuenta(TipoCuenta tipoCuenta, Pageable pageable);
    
    @Query("SELECT c FROM Cuenta c WHERE c.estadoCuenta = :estado AND c.tipoCuenta = :tipo")
    Page<Cuenta> findByEstadoCuentaAndTipoCuenta(@Param("estado") Boolean estadoCuenta, 
                                                 @Param("tipo") TipoCuenta tipoCuenta, 
                                                 Pageable pageable);
    
    @Query("SELECT c FROM Cuenta c JOIN c.usuarios u WHERE u.id = :usuarioId")
    Page<Cuenta> findByUsuarioId(@Param("usuarioId") Long usuarioId, Pageable pageable);
}
