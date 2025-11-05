package edu.empresa.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estudiante_carrera")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteCarrera {
    @EmbeddedId
    private EstudianteCarreraId id;

    @ManyToOne
    @MapsId("estudianteDni")
    @JoinColumn(name = "id_estudiante", referencedColumnName = "dni")
    private Estudiante estudiante;

    @ManyToOne
    @MapsId("carreraId")
    @JoinColumn(name = "id_carrera", referencedColumnName = "id_carrera")
    private Carrera carrera;

    @Column(nullable = false)
    private int inscripcion;
    @Column(nullable = true)
    private int graduacion;
    @Column
    private int antiguedad;

    // Constructor personalizado para inicializar la clave compuesta
    public EstudianteCarrera(Estudiante estudiante, Carrera carrera, int inscripcion, int graduacion, int antiguedad) {
        this.id = new EstudianteCarreraId(estudiante.getDni(), carrera.getId_carrera());
        this.estudiante = estudiante;
        this.carrera = carrera;
        this.inscripcion = inscripcion;
        this.graduacion = graduacion;
        this.antiguedad = antiguedad;
    }
}
