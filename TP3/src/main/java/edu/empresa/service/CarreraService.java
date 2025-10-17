package edu.empresa.service;

import edu.empresa.dto.CarreraDTO;
import edu.empresa.dto.GenerarReporteDTO;
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

    /**
     * Genera un reporte completo de carreras con inscriptos y egresados por año
     * @return Lista de GenerarReporteDTO ordenada alfabéticamente por carrera y cronológicamente por año
     */
    @Transactional
    public List<GenerarReporteDTO> generarReporteCarreras() {
        List<Object[]> resultados = repository.generarReporteCarreras();
        
        return resultados.stream()
            .map(row -> new GenerarReporteDTO(
                (String) row[0],      // nombreCarrera
                (Integer) row[1],     // idCarrera
                (Integer) row[2],     // anio
                ((Number) row[3]).longValue(),  // inscriptos
                ((Number) row[4]).longValue()   // egresados
            ))
            .toList();
    }
}
