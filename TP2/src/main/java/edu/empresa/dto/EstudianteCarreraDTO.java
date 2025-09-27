package edu.empresa.dto;

import java.time.LocalDate;

public class EstudianteCarreraDTO {
    private int id;
    private int idEstudiante;
    private int idCarrera;
    private int inscripcion;
    private int graduacion;
    private int antiguedad;

    public EstudianteCarreraDTO(int id, int idEstudiante, int idCarrera, int inscripcion, int graduacion, int antiguedad) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.idCarrera = idCarrera;
        this.inscripcion = inscripcion;
        this.graduacion = graduacion;
        this.antiguedad = antiguedad;
    }

    public int getId() {
        return id;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public int getIdCarrera() {
        return idCarrera;
    }

    public int getInscripcion() {
        return inscripcion;
    }

    public int getGraduacion() {
        return graduacion;
    }

    public int getAntiguedad() {
        return antiguedad;
    }
}
