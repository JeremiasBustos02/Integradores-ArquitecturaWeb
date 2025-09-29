package edu.empresa.dto;

public class CarreraDTO {
    private int idCarrera;
    private String nombre;
    private int duracion;

    public CarreraDTO() {
        super();
    }

    public CarreraDTO(int idCarrera, String nombre, int duracion) {
        this.idCarrera = idCarrera;
        this.nombre = nombre;
        this.duracion = duracion;
    }

    public int getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    @Override
    public String toString() {
        return "CarreraDTO{" +
                "idCarrera=" + idCarrera +
                ", nombre='" + nombre + '\'' +
                ", duracion=" + duracion +
                '}';
    }
}
