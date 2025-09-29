package edu.empresa.repositories;

import edu.empresa.dto.EstudianteDTO;
import edu.empresa.entities.Estudiante;

import java.util.List;

public interface EstudianteRepository {
    // Métodos que manejan entidades (para operaciones internas)
    Estudiante altaEstudiante(int dni,String nombre, String apellido, int edad, String genero, int lu, String ciudad);
    Estudiante buscarPorDNI(int dni);

    EstudianteDTO buscarPorLU(int lu);
    List<EstudianteDTO> buscarTodos();
    List<EstudianteDTO> buscarPorGenero(String genero);
    List<EstudianteDTO> buscarTodosOrderByNombre();
    List<EstudianteDTO> buscarTodosPorCarreraYCiudad(int idCarrera, String ciudad);
}
