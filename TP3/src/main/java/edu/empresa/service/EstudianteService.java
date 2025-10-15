package edu.empresa.service;

import edu.empresa.repository.EstudianteRepository;
import org.springframework.stereotype.Service;

@Service("estudianteService")
public class EstudianteService {
    private final EstudianteRepository repository;

    public EstudianteService(EstudianteRepository repository) {
        this.repository = repository;
    }
}
