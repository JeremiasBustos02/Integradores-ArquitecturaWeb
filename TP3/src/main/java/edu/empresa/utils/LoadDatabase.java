/*package edu.empresa.utils;

import edu.empresa.dto.EstudianteCarreraDTO;
import edu.empresa.model.Carrera;
import edu.empresa.model.Estudiante;
import edu.empresa.model.EstudianteCarrera;
import edu.empresa.repository.CarreraRepository;
import edu.empresa.repository.EstudianteCarreraRepository;
import edu.empresa.repository.EstudianteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Configuration
@Slf4j
public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(
            @Qualifier("estudianteRepository") EstudianteRepository estudianteRepository,
            @Qualifier("carreraRepository") CarreraRepository carreraRepository,
            @Qualifier("estudianteCarreraRepository") EstudianteCarreraRepository estudianteCarreraRepository
    ) {
        return args -> {
            log.info("📦 Cargando datos desde archivos CSV...");

            CSVReader lector = new CSVReader();

            // 1️⃣ Cargar carreras
            List<Carrera> carreras = lector.leerArchivoCarreras("src/main/resources/csv/carreras.csv");
            carreraRepository.saveAll(carreras);
            log.info("✅ Carreras cargadas: {}", carreras.size());

            // 2️⃣ Cargar estudiantes
            List<Estudiante> estudiantes = lector.leerArchivoEstudiantes("src/main/resources/csv/estudiantes.csv");
            estudianteRepository.saveAll(estudiantes);
            log.info("✅ Estudiantes cargados: {}", estudiantes.size());

            // 3️⃣ Cargar relaciones estudiante-carrera
            List<EstudianteCarreraDTO> inscripciones = lector.leerArchivoEstudiantesCarreras("src/main/resources/csv/estudianteCarrera.csv");
            for (EstudianteCarreraDTO dto : inscripciones) {
                Optional<Estudiante> estudianteOpt = estudianteRepository.findById(dto.getIdEstudiante());
                Optional<Carrera> carreraOpt = carreraRepository.findById(dto.getIdCarrera());

                if (estudianteOpt.isPresent() && carreraOpt.isPresent()) {
                    EstudianteCarrera ec = new EstudianteCarrera();
                    ec.setId(dto.getId()); // solo si querés mantener el mismo ID del CSV
                    ec.setEstudiante(estudianteOpt.get());
                    ec.setCarrera(carreraOpt.get());
                    ec.setInscripcion(dto.getInscripcion());
                    ec.setGraduacion(dto.getGraduacion());
                    ec.setAntiguedad(dto.getAntiguedad());
                    estudianteCarreraRepository.save(ec);
                } else {
                    log.warn("⚠️ No se encontró estudiante o carrera para la relación ID={} (estudiante={}, carrera={})",
                            dto.getId(), dto.getIdEstudiante(), dto.getIdCarrera());
                }
            }

            log.info("✅ Relaciones EstudianteCarrera cargadas: {}", inscripciones.size());
            log.info("🎉 Base de datos inicializada correctamente.");

        };
    }
}
*/