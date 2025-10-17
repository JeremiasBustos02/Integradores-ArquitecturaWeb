package edu.empresa.service;

import edu.empresa.dto.EstudianteDTO;
import edu.empresa.model.Estudiante;
import edu.empresa.repository.EstudianteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("estudianteService")
public class EstudianteService {
    @Autowired
    private final EstudianteRepository repository;

    public EstudianteService(EstudianteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<EstudianteDTO> getEstudiantes() {
        return repository.findAll().stream().map(estudiante -> new EstudianteDTO(estudiante.getDni(),
                estudiante.getNombre(),
                estudiante.getApellido(),
                estudiante.getEdad(),
                estudiante.getGenero(),
                estudiante.getLu(),
                estudiante.getCiudad())).toList();
    }

    /**
     * Da de alta un nuevo estudiante en el sistema
     * @param dto DTO con los datos del estudiante
     * @return EstudianteDTO con los datos del estudiante creado
     * @throws RuntimeException si el estudiante ya existe (DNI o LU duplicado)
     */
    @Transactional
    public EstudianteDTO altaEstudiante(EstudianteDTO dto) {
        // Verificar que el DNI no exista ya
        if (repository.existsById(dto.getDni())) {
            throw new RuntimeException("Ya existe un estudiante con DNI " + dto.getDni());
        }

        // Verificar que el LU no exista ya (es unique)
        if (repository.findAll().stream().anyMatch(e -> e.getLu() == dto.getLu())) {
            throw new RuntimeException("Ya existe un estudiante con el número de legajo (LU) " + dto.getLu());
        }

        // Crear el nuevo estudiante
        Estudiante estudiante = new Estudiante(
            dto.getDni(),
            dto.getNombre(),
            dto.getApellido(),
            dto.getEdad(),
            dto.getGenero(),
            dto.getLu(),
            dto.getCiudad()
        );

        // Guardar en la base de datos
        Estudiante saved = repository.save(estudiante);

        // Convertir a DTO para retornar
        return new EstudianteDTO(
            saved.getDni(),
            saved.getNombre(),
            saved.getApellido(),
            saved.getEdad(),
            saved.getGenero(),
            saved.getLu(),
            saved.getCiudad()
        );
    }

}
