package edu.empresa.service;

import edu.empresa.repository.EstudianteCarreraRepository;
import org.springframework.stereotype.Service;

@Service("estudianteCarreraService")
public class EstudianteCarreraService {
    private final EstudianteCarreraRepository repository;

    public EstudianteCarreraService(EstudianteCarreraRepository repository) {
        this.repository = repository;
    }
}
