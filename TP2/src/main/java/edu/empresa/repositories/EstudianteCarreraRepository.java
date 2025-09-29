package edu.empresa.repositories;

import edu.empresa.entities.Carrera;
import edu.empresa.entities.Estudiante;

import java.time.LocalDate;

public interface EstudianteCarreraRepository {
    void anotarEstudiante(Estudiante e, Carrera c, int inscripcion, int graduacion, int antiguedad);
}
