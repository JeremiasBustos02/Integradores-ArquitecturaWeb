package edu.empresa.repositories;

import edu.empresa.entities.Carrera;
import edu.empresa.entities.Estudiante;

import java.time.LocalDate;

public interface EstudianteCarreraRepository {
    void anotarEstudiante(int id , Estudiante e, Carrera c, LocalDate inscripcion,LocalDate graduacion,int antiguedad);
}
