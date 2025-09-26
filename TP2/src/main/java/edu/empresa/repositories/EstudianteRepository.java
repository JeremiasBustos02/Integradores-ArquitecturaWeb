package edu.empresa.repositories;

import edu.empresa.entities.Estudiante;

import java.util.List;

public interface EstudianteRepository {

    Estudiante buscarPorLU(int lu);
    List<Estudiante> buscarTodos();

}
