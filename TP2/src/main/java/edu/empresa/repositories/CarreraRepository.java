package edu.empresa.repositories;

import edu.empresa.dto.CarreraDTO;
import edu.empresa.dto.GenerarReporteDTO;
import edu.empresa.entities.Carrera;
import edu.empresa.entities.Estudiante;

import java.util.List;

public interface CarreraRepository {
    // Métodos que manejan entidades (para operaciones internas)
    List<Estudiante> getCarrerasConEstudiantes();

    List<Estudiante> getEstudiantesByCarrera(Carrera c, String ciudad);

    void altaCarrera(int id, String nombre, int duracion);

    Carrera buscarCarreraPorId(int id); // Para matriculación (retorna entidad)

    List<CarreraDTO> obtenerTodasCarreras();

    public List<GenerarReporteDTO> generarReporteCarreras();

    // Método que retornEa DTO (para capa de presentación)
    CarreraDTO buscarPorId(int id);

    List<CarreraDTO> recuperarCarrerasConInscriptos();
}
