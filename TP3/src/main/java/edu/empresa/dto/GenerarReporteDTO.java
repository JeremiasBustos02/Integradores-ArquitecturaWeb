package edu.empresa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerarReporteDTO {
    private String nombreCarrera;
    private int idCarrera;
    private int anio;
    private long inscriptos;
    private long egresados;
}
