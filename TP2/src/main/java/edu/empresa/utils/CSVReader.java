package edu.empresa.utils;

import edu.empresa.entities.Carrera;
import edu.empresa.entities.Estudiante;
import edu.empresa.entities.EstudianteCarrera;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class CSVReader {

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

    public List<EstudianteCarrera> leerArchivoEstudiantesCarreras(
            String rutaArchivo,
            List<Estudiante> estudiantes,
            List<Carrera> carreras) {
        List<EstudianteCarrera> estudiantesCarreras = new ArrayList<>();

        // Mapas para acceder rápido a los objetos por ID
        Map<Integer, Estudiante> mapaEstudiantes = estudiantes.stream()
                .collect(Collectors.toMap(Estudiante::getDni, e -> e));
        Map<Integer, Carrera> mapaCarreras = carreras.stream()
                .collect(Collectors.toMap(Carrera::getIdCarrera, c -> c));


        try (CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader(rutaArchivo))) {
            for (CSVRecord record : parser) {
                int id = Integer.parseInt(record.get("id"));
                int idEstudiante = Integer.parseInt(record.get("id_estudiante"));
                int idCarrera = Integer.parseInt(record.get("id_carrera"));

                // Convertir las fechas
                String inscripcionStr = record.get("inscripcion");
                String graduacionStr = record.get("graduacion");

                LocalDate inscripcion = LocalDate.of(Integer.parseInt(inscripcionStr), 1, 1); // año, mes, día
                LocalDate graduacion = graduacionStr.equals("0") ? null : LocalDate.of(Integer.parseInt(graduacionStr), 1, 1);

                int antiguedad = Integer.parseInt(record.get("antiguedad"));

                // Buscar objetos por ID
                Estudiante e = mapaEstudiantes.get(idEstudiante);
                Carrera c = mapaCarreras.get(idCarrera);

                // Crear objeto EstudianteCarrera
                EstudianteCarrera ec = new EstudianteCarrera(id, e, c, inscripcion, graduacion, antiguedad);
                estudiantesCarreras.add(ec);
            }
            System.out.println("Archivo estudiante_carrera.csv leído correctamente.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return estudiantesCarreras;
    }
}
