package edu.empresa.service;

import edu.empresa.dto.CarreraDTO;
import edu.empresa.repository.CarreraRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("carreraService")
public class CarreraService {
    @Autowired
    private final CarreraRepository repository;

    public CarreraService(CarreraRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<CarreraDTO> getCarrerasConInscriptos(){
        return repository.getCarrerasConInscriptos();
    }
    @Transactional
    public List<CarreraDTO> getCarreras(){
        return repository.findAll().stream().map(carrera -> new CarreraDTO(carrera.getId_carrera(),carrera.getNombre(),carrera.getDuracion())).toList();
    }
}
