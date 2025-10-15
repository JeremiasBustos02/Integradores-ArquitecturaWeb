package edu.empresa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteCarreraDTO {
    private int id;
    private int idEstudiante;
    private int idCarrera;
    private int inscripcion;
    private int graduacion;
    private int antiguedad;
}
