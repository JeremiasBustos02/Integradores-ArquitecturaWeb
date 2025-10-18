package edu.empresa.utils;


import edu.empresa.dto.EstudianteCarreraDTO;
import edu.empresa.model.Carrera;
import edu.empresa.model.Estudiante;
import edu.empresa.model.EstudianteCarrera;
import edu.empresa.repository.CarreraRepository;
import edu.empresa.repository.EstudianteCarreraRepository;
import edu.empresa.repository.EstudianteRepository;
import edu.empresa.service.EstudianteCarreraService;
import jakarta.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class CSVReader {
    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private EstudianteCarreraService estudianteCarreraService;

    @Transactional
    public void cargarDatos() throws IOException {
        System.out.printf("---CARGANDO DATOS---");
        cargarCarrera();
        cargarEstudiantes();
        //cargarEstudianteCarrera();
        System.out.printf("---CARGA COMPLETADA---");

    }
    public void cargarCarrera() throws IOException {
        //Cambiar path por absoluth si no funciona
        File archivoCSV = ResourceUtils.getFile("src/main/resources/csv/carreras.csv");

        try (FileReader reader = new FileReader(archivoCSV);
             CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

            for (CSVRecord csvRecord : csvParser) {
                Carrera c = new Carrera();
                c.setNombre(csvRecord.get("carrera"));
                int id = Integer.parseInt(csvRecord.get("id_carrera"));
                c.setId_carrera(id);
                int duracion = Integer.parseInt(csvRecord.get("duracion"));
                c.setDuracion(duracion);

                carreraRepository.save(c);
            }
        }
    }

    public void cargarEstudiantes() throws IOException {
        //Cambiar path por absoluth si no funciona
        File archivoCSV = ResourceUtils.getFile("src/main/resources/csv/estudiantes.csv");

        try (FileReader reader = new FileReader(archivoCSV);
             CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

            for (CSVRecord csvRecord : csvParser) {
                Estudiante e = new Estudiante();

                int dni = Integer.parseInt(csvRecord.get("DNI"));
                e.setDni(dni);
                e.setNombre(csvRecord.get("nombre"));
                e.setApellido(csvRecord.get("apellido"));
                int edad = Integer.parseInt(csvRecord.get("edad"));
                e.setEdad(edad);
                e.setGenero(csvRecord.get("genero"));
                e.setCiudad(csvRecord.get("ciudad"));
                int legajo = Integer.parseInt(csvRecord.get("LU"));
                e.setLu(legajo);


                estudianteRepository.save(e);
            }
        }
    }

    public void cargarEstudianteCarrera() throws IOException {
        ClassPathResource resource = new ClassPathResource("csv/estudianteCarrera.csv");

        try (Reader reader = new InputStreamReader(resource.getInputStream());
             CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

            int contador = 0;
            int errores = 0;

            for (CSVRecord csvRecord : csvParser) {
                try {
                    EstudianteCarreraDTO dto = new EstudianteCarreraDTO();
                    dto.setId(Integer.parseInt(csvRecord.get("id")));
                    dto.setIdEstudiante(Integer.parseInt(csvRecord.get("id_estudiante")));
                    dto.setIdCarrera(Integer.parseInt(csvRecord.get("id_carrera")));
                    dto.setInscripcion(Integer.parseInt(csvRecord.get("inscripcion")));
                    dto.setGraduacion(Integer.parseInt(csvRecord.get("graduacion")));
                    dto.setAntiguedad(Integer.parseInt(csvRecord.get("antiguedad")));

                    // ✅ Usar el service inyectado
                    estudianteCarreraService.inscribirEstudianteEnCarrera(dto);
                    contador++;

                } catch (RuntimeException e) {
                    errores++;
                    System.err.println("Error en línea " + csvParser.getCurrentLineNumber() +
                            ": " + e.getMessage());
                    // Continuar con el siguiente registro
                }
            }

            System.out.println("Inscripciones cargadas: " + contador + ", errores: " + errores);
        }
    }

    public List<Carrera> leerArchivoCarreras(String rutaArchivo) {
        List<Carrera> carreras = new ArrayList<>();

        try (CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader(rutaArchivo))) {
            for (CSVRecord record : parser) {
                int id_carrera = Integer.parseInt(record.get("id_carrera"));
                String nombre = record.get("carrera");
                int duracion = Integer.parseInt(record.get("duracion"));
                Carrera c = new Carrera(id_carrera, nombre, duracion);
                carreras.add(c);
            }
            System.out.println("Archivo carreras.csv leído correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return carreras;
    }

    public List<Estudiante> leerArchivoEstudiantes(String rutaArchivo) {
        List<Estudiante> estudiantes = new ArrayList<>();

        try (CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader(rutaArchivo))) {
            for (CSVRecord record : parser) {
                int dni = Integer.parseInt(record.get("DNI"));
                String nombre = record.get("nombre");
                String apellido = record.get("apellido");
                int edad = Integer.parseInt(record.get("edad"));
                String genero = record.get("genero");
                String ciudad = record.get("ciudad");
                int lu = Integer.parseInt(record.get("LU"));
                Estudiante e = new Estudiante(dni, nombre, apellido, edad, genero, lu, ciudad);
                estudiantes.add(e);
            }
            System.out.println("Archivo estudiantes.csv leído correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return estudiantes;
    }

    public List<EstudianteCarreraDTO> leerArchivoEstudiantesCarreras(String rutaArchivo) {
        List<EstudianteCarreraDTO> estudiantesCarreras = new ArrayList<>();

        try (CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader(rutaArchivo))) {
            for (CSVRecord record : parser) {
                int id = Integer.parseInt(record.get("id"));
                int idEstudiante = Integer.parseInt(record.get("id_estudiante"));
                int idCarrera = Integer.parseInt(record.get("id_carrera"));
                int inscripcion = Integer.parseInt(record.get("inscripcion"));
                int graduacion = Integer.parseInt(record.get("graduacion"));
                int antiguedad = Integer.parseInt(record.get("antiguedad"));

                EstudianteCarreraDTO ecDTO = new EstudianteCarreraDTO(id, idEstudiante, idCarrera, inscripcion, graduacion, antiguedad);
                estudiantesCarreras.add(ecDTO);
            }
            System.out.println("Archivo estudiante_carrera.csv leído correctamente.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return estudiantesCarreras;
    }
}
