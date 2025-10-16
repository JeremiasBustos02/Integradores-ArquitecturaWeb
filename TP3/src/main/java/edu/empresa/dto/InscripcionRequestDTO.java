package edu.empresa.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO simplificado para solicitudes de inscripción de estudiante en carrera
 */
public class InscripcionRequestDTO {
    
    @NotNull(message = "El DNI del estudiante es obligatorio")
    @Positive(message = "El DNI del estudiante debe ser un número positivo")
    private Integer idEstudiante; // DNI del estudiante
    
    @NotNull(message = "El ID de la carrera es obligatorio")
    @Positive(message = "El ID de la carrera debe ser un número positivo")
    private Integer idCarrera;    // ID de la carrera

    // Constructor por defecto
    public InscripcionRequestDTO() {
    }

    // Constructor con parámetros
    public InscripcionRequestDTO(Integer idEstudiante, Integer idCarrera) {
        this.idEstudiante = idEstudiante;
        this.idCarrera = idCarrera;
    }

    // Getters y setters
    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Integer getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(Integer idCarrera) {
        this.idCarrera = idCarrera;
    }
}
