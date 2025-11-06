package com.microservices.usuarios.repository;

import com.microservices.usuarios.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    Optional<Usuario> findByCelular(String celular);
    
    boolean existsByEmail(String email);
    
    boolean existsByCelular(String celular);
    
    @Query("SELECT u FROM Usuario u WHERE u.nombre LIKE %:nombre% OR u.apellido LIKE %:apellido%")
    Page<Usuario> findByNombreOrApellidoContaining(@Param("nombre") String nombre, 
                                                   @Param("apellido") String apellido, 
                                                   Pageable pageable);
    
    @Query("SELECT u FROM Usuario u JOIN u.cuentas c WHERE c.id = :cuentaId")
    Page<Usuario> findByCuentaId(@Param("cuentaId") Long cuentaId, Pageable pageable);
}
