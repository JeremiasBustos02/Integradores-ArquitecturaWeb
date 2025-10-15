package edu.empresa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteDTO {
    private int dni;
    private String nombre;
    private String apellido;
    private int edad;
    private String genero;
    private int lu;
    private String ciudad;
}
