package edu.empresa.repository;

import edu.empresa.dto.EstudianteDTO;
import edu.empresa.model.Estudiante;
import edu.empresa.model.EstudianteCarrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EstudianteCarreraRepository extends JpaRepository<EstudianteCarrera, Integer> {
    
    /**
     * Verifica si existe una inscripción para un estudiante en una carrera específica
     * @param dni DNI del estudiante
     * @param idCarrera ID de la carrera
     * @return true si existe la inscripción, false en caso contrario
     */
    @Query("SELECT CASE WHEN COUNT(ec) > 0 THEN true ELSE false END FROM EstudianteCarrera ec WHERE ec.estudiante.dni = :dni AND ec.carrera.id_carrera = :idCarrera")
    boolean existeInscripcion(@Param("dni") int dni, @Param("idCarrera") int idCarrera);
    
    /**
     * Obtiene los estudiantes de una carrera específica, filtrados por ciudad de residencia
     * @param idCarrera ID de la carrera
     * @param ciudad ciudad de residencia
     * @return lista de estudiantes
     */
    @Query("SELECT ec.estudiante FROM EstudianteCarrera ec " +
           "WHERE ec.carrera.id_carrera = :idCarrera " +
           "AND ec.estudiante.ciudad = :ciudad " +
           "ORDER BY ec.estudiante.apellido, ec.estudiante.nombre")
    List<Estudiante> findEstudiantesByCarreraAndCiudad(@Param("idCarrera") int idCarrera, @Param("ciudad") String ciudad);
}
