package edu.empresa.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Estudiante_Carrera")
public class EstudianteCarrera {
    @Id
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_estudiante", referencedColumnName = "dni")
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "id_carrera", referencedColumnName = "id_carrera")
    private Carrera carrera;

    @Column(nullable = false)
    private LocalDate inscripcion;

    @Column(nullable = true)
    private LocalDate graduacion;

    @Column
    private int antiguedad;

    public EstudianteCarrera() {
        super();
    }

    public EstudianteCarrera(int id, Estudiante estudiante, Carrera carrera, LocalDate inscripcion, LocalDate graduacion, int antiguedad) {
        this.id = id;
        this.estudiante = estudiante;
        this.carrera = carrera;
        this.inscripcion = inscripcion;
        this.graduacion = graduacion;
        this.antiguedad = antiguedad;
    }

}
