package edu.empresa.repository;

import edu.empresa.dto.CarreraDTO;
import edu.empresa.dto.GenerarReporteDTO;
import edu.empresa.model.Carrera;
import edu.empresa.model.Estudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("carreraRepository")
public interface CarreraRepository extends JpaRepository<Carrera, Integer> {

    /**
     * Obtiene todas las carreras con sus estudiantes inscriptos, ordenadas por cantidad de inscriptos (descendente)
     * @return Lista de CarreraDTO con información completa de cada carrera y su cantidad de inscriptos
     */
    @Query("""
    SELECT new edu.empresa.dto.CarreraDTO(c.id_carrera, c.nombre, c.duracion, COUNT(e))
    FROM EstudianteCarrera ec
    JOIN ec.carrera c 
    JOIN ec.estudiante e
    GROUP BY c.id_carrera, c.nombre, c.duracion
    ORDER BY COUNT(e) DESC
    """)
    List<CarreraDTO> getCarrerasConInscriptos();

    /**
     * Genera un reporte completo de carreras con inscriptos y egresados por año
     * Para cada año muestra:
     * - Cuántos estudiantes se inscribieron ese año
     * - Cuántos estudiantes egresaron ese año
     * Ordenado alfabéticamente por carrera y cronológicamente por año
     * @return Lista de objetos con los datos del reporte
     */
    @Query(value = """
    SELECT c.nombre as nombreCarrera,
           c.id_carrera as idCarrera,
           anios.anio as anio,
           COALESCE(i.inscriptos, 0) as inscriptos,
           COALESCE(e.egresados, 0) as egresados
    FROM carrera c
    CROSS JOIN (
        SELECT DISTINCT inscripcion as anio FROM estudiante_carrera
        UNION
        SELECT DISTINCT graduacion FROM estudiante_carrera WHERE graduacion > 0
    ) anios
    LEFT JOIN (
        SELECT id_carrera, inscripcion as anio, COUNT(*) as inscriptos
        FROM estudiante_carrera
        GROUP BY id_carrera, inscripcion
    ) i ON c.id_carrera = i.id_carrera AND anios.anio = i.anio
    LEFT JOIN (
        SELECT id_carrera, graduacion as anio, COUNT(*) as egresados
        FROM estudiante_carrera
        WHERE graduacion > 0
        GROUP BY id_carrera, graduacion
    ) e ON c.id_carrera = e.id_carrera AND anios.anio = e.anio
    WHERE COALESCE(i.inscriptos, 0) > 0 OR COALESCE(e.egresados, 0) > 0
    ORDER BY c.nombre ASC, anios.anio ASC
    """, nativeQuery = true)
    List<Object[]> generarReporteCarreras();
}
