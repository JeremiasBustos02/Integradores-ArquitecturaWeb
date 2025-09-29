package edu.empresa.repositories;

import edu.empresa.dto.CarreraDTO;
import edu.empresa.entities.Carrera;
import edu.empresa.entities.Estudiante;

import java.time.LocalDate;
import java.util.List;

public interface EstudianteCarreraRepository {
    void anotarEstudiante(Estudiante e, Carrera c, int inscripcion, int graduacion, int antiguedad);
}
