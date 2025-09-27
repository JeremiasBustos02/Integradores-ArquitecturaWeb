package edu.empresa.utils;

import edu.empresa.entities.Carrera;
import edu.empresa.entities.Estudiante;
import edu.empresa.entities.EstudianteCarrera;
import edu.empresa.factories.DAOFactory;
import edu.empresa.factories.JPAUtil;
import edu.empresa.repositories.CarreraRepository;
import edu.empresa.repositories.EstudianteCarreraRepository;
import edu.empresa.repositories.EstudianteRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class DataLoader {
    private EstudianteRepository estudianteRepository;
    private CarreraRepository carreraRepository;
    private EstudianteCarreraRepository estudianteCarreraRepository;
    private CSVReader csvReader;

    public DataLoader() {
        DAOFactory daoFactory = DAOFactory.getInstance();
        EntityManager em = JPAUtil.getEntityManager();
        this.estudianteRepository = daoFactory.getEstudianteDAO(em);
        this.carreraRepository = daoFactory.getCarreraDAO(em);
        this.estudianteCarreraRepository = daoFactory.getEstudianteCarreraDAO(em);
        this.csvReader = new CSVReader();
    }

    public void loadData() {
        List<Estudiante> estudiantes = loadEsstudiantes("src/main/resources/csv_files/estudiantes.csv");
        List<Carrera> carreras = loadCarreras("src/main/resources/csv_files/carreras.csv");
        loadEstudianteCarrera("src/main/resources/csv_files/estudianteCarrera.csv", estudiantes, carreras);
    }

    private List<Estudiante> loadEsstudiantes(String filePath) {
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
        return estudiantes;
    }

    private List<Carrera> loadCarreras(String filePath) {
        List<Carrera> carreras = csvReader.leerArchivoCarreras(filePath);
        for (Carrera carrera : carreras) {
            carreraRepository.altaCarrera(
                    carrera.getIdCarrera(),
                    carrera.getNombre(),
                    carrera.getDuracion()
            );
        }
        return carreras;
    }

    private void loadEstudianteCarrera(String filePath, List<Estudiante> estudiantes, List<Carrera> carreras) {
        List<EstudianteCarrera> estudianteCarreras = csvReader.leerArchivoEstudiantesCarreras(filePath, estudiantes, carreras);
        for (EstudianteCarrera estudianteCarrera : estudianteCarreras) {
            estudianteCarreraRepository.anotarEstudiante(
                    estudianteCarrera.getId(),
                    estudianteCarrera.getEstudiante(),
                    estudianteCarrera.getCarrera(),
                    estudianteCarrera.getInscripcion(),
                    estudianteCarrera.getGraduacion(),
                    estudianteCarrera.getAntiguedad()
            );
        }
    }
}
