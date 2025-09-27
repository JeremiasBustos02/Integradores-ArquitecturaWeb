package edu.empresa.repositories;

import edu.empresa.entities.Estudiante;

import java.util.List;

public interface EstudianteRepository {
    Estudiante altaEstudiante(int dni,String nombre, String apellido, int edad, String genero, int lu, String ciudad);

    Estudiante buscarPorDNI(int dni);

    Estudiante buscarPorLU(int lu);

    List<Estudiante> buscarTodos();

    List<Estudiante> buscarPorGenero(String genero);

    List<Estudiante> buscarTodosOrderByNombre();
}
