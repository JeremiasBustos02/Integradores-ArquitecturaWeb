package edu.empresa.service;

import edu.empresa.repository.CarreraRepository;
import org.springframework.stereotype.Service;

@Service("carreraService")
public class CarreraService {
    private final CarreraRepository repository;

    public CarreraService(CarreraRepository repository) {
        this.repository = repository;
    }
}
