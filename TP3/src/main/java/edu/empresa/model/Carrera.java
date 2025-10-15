package edu.empresa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carrera {
    @Id
    private int id_carrera;
    @Column
    private String nombre;
    @Column
    private int duracion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "carrera")
    private List<EstudianteCarrera> estudiantes = new ArrayList<>();

    public Carrera(int id_carrera, String nombre, int duracion) {
        this.id_carrera = id_carrera;
        this.nombre = nombre;
        this.duracion = duracion;
        this.estudiantes = new ArrayList<>();
    }


}
