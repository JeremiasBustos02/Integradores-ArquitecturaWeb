package edu.empresa.service;

import edu.empresa.dto.EstudianteDTO;
import edu.empresa.model.Estudiante;
import edu.empresa.repository.EstudianteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service("estudianteService")
public class EstudianteService {
    @Autowired
    private final EstudianteRepository repository;

    public EstudianteService(EstudianteRepository repository) {
        this.repository = repository;
    }

    /**
     * Devuelve una lista DTO de estudiantes ordenados alfabeticamente
     * por apellido
     */
    @Transactional
    public List<EstudianteDTO> getEstudiantes() {
        return repository.getEstudianteByAlphabetOrder().stream().map(e -> new EstudianteDTO(
                e.getDni(), e.getNombre(), e.getApellido(), e.getEdad(), e.getGenero(), e.getLu(), e.getCiudad()
        )).toList();
    }

    /**
     * Da de alta un nuevo estudiante en el sistema
     *
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
        if (repository.findByLu(dto.getLu()).isPresent()) {
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

    /**
     * Busca un estudiante por su número de libreta universitaria (LU)
     *
     * @param lu número de libreta universitaria
     * @return EstudianteDTO si se encuentra el estudiante
     * @throws RuntimeException si no se encuentra el estudiante
     */
    @Transactional
    public EstudianteDTO getEstudianteByLU(int lu) {
        Estudiante estudiante = repository.findByLu(lu)
                .orElseThrow(() -> new RuntimeException("No se encontró estudiante con número de libreta (LU) " + lu));

        return new EstudianteDTO(
                estudiante.getDni(),
                estudiante.getNombre(),
                estudiante.getApellido(),
                estudiante.getEdad(),
                estudiante.getGenero(),
                estudiante.getLu(),
                estudiante.getCiudad()
        );
    }

    /**
     * Busca estudiantes por género
     *
     * @param genero género a buscar (M/F)
     * @return lista de estudiantes ordenados por apellido y nombre
     */
    @Transactional
    public List<EstudianteDTO> getEstudiantesByGenero(String genero) {
        return repository.findByGenero(genero).stream()
                .sorted(Comparator.comparing(Estudiante::getApellido)
                        .thenComparing(Estudiante::getNombre))
                .map(estudiante -> new EstudianteDTO(
                        estudiante.getDni(),
                        estudiante.getNombre(),
                        estudiante.getApellido(),
                        estudiante.getEdad(),
                        estudiante.getGenero(),
                        estudiante.getLu(),
                        estudiante.getCiudad()))
                .toList();
    }

}
