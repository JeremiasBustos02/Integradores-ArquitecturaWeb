package edu.empresa.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Estudiante")
public class Estudiante {
    @Id
    private int dni;
    @Column
    private String nombre;
    @Column
    private String apellido;
    @Column
    private int edad;
    @Column
    private String genero;
    @Column(unique = true)
    private int lu;
    @Column
    private String ciudad;

    @OneToMany(mappedBy = "estudiante")
    private List<EstudianteCarrera> carreras = new ArrayList<>();

    public Estudiante() {
        super();
    }

    public Estudiante(int dni, String nombre, String apellido, int edad, String genero, int lu, String ciudad) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.genero = genero;
        this.lu = lu;
        this.ciudad = ciudad;
    }

    public int getDni() {
        return dni;
    }
}
