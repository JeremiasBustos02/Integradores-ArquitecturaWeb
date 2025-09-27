package edu.empresa.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Carrera")
public class Carrera {
    @Id
    private int id_carrera;
    @Column
    private String nombre;
    @Column
    private int duracion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "carrera")
    private List<EstudianteCarrera> estudiantes = new ArrayList<>();

    public Carrera() {
        super();
    }

    public Carrera(int id_carrera, String nombre, int duracion) {
        this.id_carrera = id_carrera;
        this.nombre = nombre;
        this.duracion = duracion;
    }

    public int getIdCarrera() {
        return id_carrera;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDuracion() {
        return duracion;
    }
}
