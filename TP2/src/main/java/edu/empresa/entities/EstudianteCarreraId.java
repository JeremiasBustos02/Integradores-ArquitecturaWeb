package edu.empresa.entities;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EstudianteCarreraId implements Serializable {
    private int estudianteDni;
    private int carreraId;

    public EstudianteCarreraId() {
    }

    public EstudianteCarreraId(int estudianteDni, int carreraId) {
        this.estudianteDni = estudianteDni;
        this.carreraId = carreraId;
    }

    public int getEstudianteDni() {
        return estudianteDni;
    }

    public void setEstudianteDni(int estudianteDni) {
        this.estudianteDni = estudianteDni;
    }

    public int getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(int carreraId) {
        this.carreraId = carreraId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstudianteCarreraId that = (EstudianteCarreraId) o;
        return estudianteDni == that.estudianteDni && carreraId == that.carreraId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(estudianteDni, carreraId);
    }
}
