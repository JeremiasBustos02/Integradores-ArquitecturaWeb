package edu.empresa.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Estudiante_Carrera")
public class EstudianteCarrera {
    @EmbeddedId
    private EstudianteCarreraId id;

    @ManyToOne
    @MapsId("estudianteDni")
    @JoinColumn(name = "id_estudiante", referencedColumnName = "dni")
    private Estudiante estudiante;

    @ManyToOne
    @MapsId("carreraId")
    @JoinColumn(name = "id_carrera", referencedColumnName = "id_carrera")
    private Carrera carrera;

    @Column(nullable = false)
    private int inscripcion;

    @Column(nullable = true)
    private int graduacion;

    @Column
    private int antiguedad;

    public EstudianteCarrera() {
        super();
    }

    public EstudianteCarrera(Estudiante estudiante, Carrera carrera, int inscripcion, int graduacion, int antiguedad) {
        this.id = new EstudianteCarreraId(estudiante.getDni(), carrera.getIdCarrera());
        this.estudiante = estudiante;
        this.carrera = carrera;
        this.inscripcion = inscripcion;
        this.graduacion = graduacion;
        this.antiguedad = antiguedad;
    }


    public EstudianteCarreraId getId() {
        return id;
    }

    public void setId(EstudianteCarreraId id) {
        this.id = id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public int getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(int inscripcion) {
        this.inscripcion = inscripcion;
    }

    public int getGraduacion() {
        return graduacion;
    }

    public void setGraduacion(int graduacion) {
        this.graduacion = graduacion;
    }

    public int getAntiguedad() {
        return antiguedad;
    }

    public void setAntiguedad(int antiguedad) {
        this.antiguedad = antiguedad;
    }
}
