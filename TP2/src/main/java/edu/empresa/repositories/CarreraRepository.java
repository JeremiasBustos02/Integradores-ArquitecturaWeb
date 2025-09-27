package edu.empresa.repositories;

import edu.empresa.entities.Carrera;
import edu.empresa.entities.Estudiante;

import java.util.List;

public interface CarreraRepository {
    List<Estudiante> getCarrerasConEstudiantes();
    List<Estudiante> getEstudiantesByCarrera(Carrera c,String ciudad);
}
