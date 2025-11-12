package com.monopatines.admin.repository;
import com.monopatines.admin.domain.PrecioVigente; 
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.data.jpa.repository.Query; 
import java.util.List;

public interface PrecioRepository extends JpaRepository<PrecioVigente, Long> {
    @Query("select p from PrecioVigente p where p.activo = true order by p.vigenteDesde desc") List<PrecioVigente> findActivos();
}