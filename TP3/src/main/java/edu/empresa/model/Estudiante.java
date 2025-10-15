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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "estudiante")
    private List<EstudianteCarrera> carreras = new ArrayList<>();

    public Estudiante(int dni, String nombre, String apellido, int edad, String genero, int lu, String ciudad) {
        super();
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.genero = genero;
        this.lu = lu;
        this.ciudad = ciudad;
        this.carreras = new ArrayList<>();
    }
}
