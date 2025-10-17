package edu.empresa.service;

import edu.empresa.dto.EstudianteDTO;
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

}
