package edu.empresa.dto;

public class GenerarReporteDTO {
    private String nombreCarrera;
    private int idCarrera;
    private int anio;
    private long inscriptos;
    private long egresados;

    // Constructor general para ambos datos
    public GenerarReporteDTO(String nombreCarrera, int idCarrera, int anio, long inscriptos, long egresados) {
        this.nombreCarrera = nombreCarrera;
        this.idCarrera = idCarrera;
        this.anio = anio;
        this.inscriptos = inscriptos;
        this.egresados = egresados;
    }

    public String getNombreCarrera() {
        return nombreCarrera;
    }
    public int getIdCarrera() {
        return idCarrera;
    }
    public int getAnio() {
        return anio;
    }
    public long getInscriptos() {
        return inscriptos;
    }
    public long getEgresados() {
        return egresados;
    }

    @Override
    public String toString() {
        return "Carrera: " + nombreCarrera +
                " | Año: " + anio +
                " | Inscriptos: " + inscriptos +
                " | Egresados: " + egresados;
    }

    public void setNombreCarrera(String nombreCarrera) {
        this.nombreCarrera = nombreCarrera;
    }

    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public void setInscriptos(long inscriptos) {
        this.inscriptos = inscriptos;
    }

    public void setEgresados(long egresados) {
        this.egresados = egresados;
    }
}