package edu.empresa.utils;

import edu.empresa.dto.EstudianteCarreraDTO;
import edu.empresa.entities.Carrera;
import edu.empresa.entities.Estudiante;
import edu.empresa.entities.EstudianteCarrera;
import edu.empresa.factories.DAOFactory;
import edu.empresa.factories.JPAUtil;
import edu.empresa.repositories.CarreraRepository;
import edu.empresa.repositories.EstudianteCarreraRepository;
import edu.empresa.repositories.EstudianteRepository;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    private EstudianteRepository estudianteRepository;
    private CarreraRepository carreraRepository;
    private EstudianteCarreraRepository estudianteCarreraRepository;
    private CSVReader csvReader;

    public DataLoader() {
        DAOFactory daoFactory = DAOFactory.getInstance();
        EntityManager em = JPAUtil.getEntityManager();
        this.estudianteRepository = daoFactory.getEstudianteRepository();
        this.carreraRepository = daoFactory.getCarreraRepository();
        this.estudianteCarreraRepository = daoFactory.getEstudianteCarreraRepository();
        this.csvReader = new CSVReader();
    }

    public void loadData() {
        //Cambiar por absolute path si no funciona
        loadEsstudiantes("src/main/resources/csv_files/estudiantes.csv");
        loadCarreras("src/main/resources/csv_files/carreras.csv");
        loadEstudianteCarrera("src/main/resources/csv_files/estudianteCarrera.csv");
    }

    private void loadEsstudiantes(String filePath) {
        List<Estudiante> estudiantes = csvReader.leerArchivoEstudiantes(filePath);
        for (Estudiante estudiante : estudiantes) {
            estudianteRepository.altaEstudiante(
                    estudiante.getDni(),
                    estudiante.getNombre(),
                    estudiante.getApellido(),
                    estudiante.getEdad(),
                    estudiante.getGenero(),
                    estudiante.getLu(),
                    estudiante.getCiudad()
            );
        }
    }

    private void loadCarreras(String filePath) {
        List<Carrera> carreras = csvReader.leerArchivoCarreras(filePath);
        for (Carrera carrera : carreras) {
            carreraRepository.altaCarrera(
                    carrera.getIdCarrera(),
                    carrera.getNombre(),
                    carrera.getDuracion()
            );
        }
    }

    private void loadEstudianteCarrera(String filePath) {
        List<EstudianteCarreraDTO> ecDTO = csvReader.leerArchivoEstudiantesCarreras(filePath);
        List<EstudianteCarrera> estudiantesCarreras = new ArrayList<>();

        for (EstudianteCarreraDTO estudianteCarreraDTO : ecDTO) {
            Estudiante estudiante = estudianteRepository.buscarPorDNI(estudianteCarreraDTO.getIdEstudiante());
            Carrera carrera = carreraRepository.buscarCarreraPorId(estudianteCarreraDTO.getIdCarrera());

            if (estudiante != null && carrera != null) {
                EstudianteCarrera ec = new EstudianteCarrera(
                        estudiante,
                        carrera,
                        estudianteCarreraDTO.getInscripcion(),
                        estudianteCarreraDTO.getGraduacion(),
                        estudianteCarreraDTO.getAntiguedad()
                );
                estudiantesCarreras.add(ec);
            }
        }

        for (EstudianteCarrera estudianteCarrera : estudiantesCarreras) {
            estudianteCarreraRepository.anotarEstudiante(
                    estudianteCarrera.getEstudiante(),
                    estudianteCarrera.getCarrera(),
                    estudianteCarrera.getInscripcion(),
                    estudianteCarrera.getGraduacion(),
                    estudianteCarrera.getAntiguedad()
            );
        }
    }
}
