package edu.empresa.service;

import edu.empresa.dto.EstudianteCarreraDTO;
import edu.empresa.dto.EstudianteDTO;
import edu.empresa.model.Carrera;
import edu.empresa.model.Estudiante;
import edu.empresa.model.EstudianteCarrera;
import edu.empresa.repository.CarreraRepository;
import edu.empresa.repository.EstudianteCarreraRepository;
import edu.empresa.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service("estudianteCarreraService")
public class EstudianteCarreraService {
    private final EstudianteCarreraRepository repository;
    private final EstudianteRepository estudianteRepository;
    private final CarreraRepository carreraRepository;
    @Autowired
    public EstudianteCarreraService(EstudianteCarreraRepository repository, 
                                  EstudianteRepository estudianteRepository,
                                  CarreraRepository carreraRepository) {
        this.repository = repository;
        this.estudianteRepository = estudianteRepository;
        this.carreraRepository = carreraRepository;
    }

    /**
     * Da de alta un estudiante en una carrera
     * @param dto DTO con los datos de la inscripción
     * @return EstudianteCarreraDTO con los datos de la inscripción creada
     * @throws RuntimeException si el estudiante o carrera no existen
     */
    public EstudianteCarreraDTO inscribirEstudianteEnCarrera(EstudianteCarreraDTO dto) {
        // Verificar que el estudiante existe
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(dto.getIdEstudiante());
        if (estudianteOpt.isEmpty()) {
            throw new RuntimeException("Estudiante con DNI " + dto.getIdEstudiante() + " no encontrado");
        }

        // Verificar que la carrera existe
        Optional<Carrera> carreraOpt = carreraRepository.findById(dto.getIdCarrera());
        if (carreraOpt.isEmpty()) {
            throw new RuntimeException("Carrera con ID " + dto.getIdCarrera() + " no encontrada");
        }

        // Verificar que el estudiante no esté ya inscrito en esa carrera
        boolean yaInscrito = repository.existeInscripcion(
            dto.getIdEstudiante(), dto.getIdCarrera());
        if (yaInscrito) {
            throw new RuntimeException("El estudiante ya está inscrito en esta carrera");
        }

        // Si no se proporciona año de inscripción, usar el año actual
        int anioInscripcion = dto.getInscripcion() != 0 ? dto.getInscripcion() : LocalDate.now().getYear();
        
        // Calcular antigüedad (años desde la inscripción)
        int antiguedad = LocalDate.now().getYear() - anioInscripcion;
        
        // Crear la nueva inscripción usando el constructor personalizado
        Estudiante estudiante = estudianteOpt.get();
        Carrera carrera = carreraOpt.get();
        EstudianteCarrera estudianteCarrera = new EstudianteCarrera(
            estudiante, carrera, anioInscripcion, dto.getGraduacion(), antiguedad);

        // Guardar en la base de datos
        EstudianteCarrera saved = repository.save(estudianteCarrera);

        // Convertir a DTO para retornar
        return new EstudianteCarreraDTO(
            saved.getEstudiante().getDni(),
            saved.getCarrera().getId_carrera(),
            saved.getInscripcion(),
            saved.getGraduacion(),
            saved.getAntiguedad()
        );
    }

    /**
     * Obtiene los estudiantes de una carrera específica, filtrados por ciudad
     * @param idCarrera ID de la carrera
     * @param ciudad ciudad de residencia
     * @return lista de EstudianteDTO ordenados por apellido y nombre
     */
    public List<EstudianteDTO> getEstudiantesByCarreraAndCiudad(int idCarrera, String ciudad) {
        return repository.findEstudiantesByCarreraAndCiudad(idCarrera, ciudad).stream()
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
