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

    @Query("""
    SELECT new edu.empresa.dto.CarreraDTO(c.nombre, COUNT(e))
    FROM EstudianteCarrera ec
    join ec.carrera c join ec.estudiante e
    GROUP BY c.nombre
    ORDER BY COUNT(e) DESC
""") List<CarreraDTO> getCarrerasConInscriptos();

   /* @Query()
    List<Estudiante> getEstudiantesByCarrera(Carrera c, String ciudad);

    void altaCarrera(int id, String nombre, int duracion);

    Carrera buscarCarreraPorId(int id); // Para matriculación (retorna entidad)

    List<CarreraDTO> obtenerTodasCarreras();

    public List<GenerarReporteDTO> generarReporteCarreras();

    // Método que retornEa DTO (para capa de presentación)
    CarreraDTO buscarPorId(int id);

    List<CarreraDTO> recuperarCarrerasConInscriptos();*/
}
