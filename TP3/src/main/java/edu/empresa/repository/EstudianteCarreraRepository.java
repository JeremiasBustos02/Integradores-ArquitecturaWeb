package edu.empresa.repository;

import edu.empresa.model.EstudianteCarrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EstudianteCarreraRepository extends JpaRepository<EstudianteCarrera, Integer> {
    
    /**
     * Verifica si existe una inscripción para un estudiante en una carrera específica
     * @param dni DNI del estudiante
     * @param idCarrera ID de la carrera
     * @return true si existe la inscripción, false en caso contrario
     */
    @Query("SELECT CASE WHEN COUNT(ec) > 0 THEN true ELSE false END FROM EstudianteCarrera ec WHERE ec.estudiante.dni = :dni AND ec.carrera.id_carrera = :idCarrera")
    boolean existeInscripcion(@Param("dni") int dni, @Param("idCarrera") int idCarrera);
}
