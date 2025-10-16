package edu.empresa.dto;

import edu.empresa.model.Carrera;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarreraDTO {
    private int idCarrera;
    private String nombre;
    private int duracion;
    private long cantidadInscriptos;

    public CarreraDTO(int idCarrera, String nombre, int duracion) {
        this.idCarrera = idCarrera;
        this.nombre = nombre;
        this.duracion = duracion;
    }
    CarreraDTO(String nombre, long cantidadInscriptos){
        this.nombre =nombre;
        this.cantidadInscriptos= cantidadInscriptos;
    }
}
