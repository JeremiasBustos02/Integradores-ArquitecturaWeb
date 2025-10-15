package edu.empresa.utils;




import edu.empresa.dto.EstudianteCarreraDTO;
import edu.empresa.model.Carrera;
import edu.empresa.model.Estudiante;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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
