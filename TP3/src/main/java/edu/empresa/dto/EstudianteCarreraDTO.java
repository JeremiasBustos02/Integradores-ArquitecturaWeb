package edu.empresa.dto;

import edu.empresa.model.EstudianteCarreraId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteCarreraDTO {
    private EstudianteCarreraId id;
    private int idEstudiante;
    private int idCarrera;
    private int inscripcion;
    private int graduacion;
    private int antiguedad;
    
    // Constructor adicional para compatibilidad
    public EstudianteCarreraDTO(int idEstudiante, int idCarrera, int inscripcion, int graduacion, int antiguedad) {
        this.id = new EstudianteCarreraId(idEstudiante, idCarrera);
        this.idEstudiante = idEstudiante;
        this.idCarrera = idCarrera;
        this.inscripcion = inscripcion;
        this.graduacion = graduacion;
        this.antiguedad = antiguedad;
    }
}
