package edu.empresa.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteCarreraId implements Serializable {
    private int estudianteDni;
    private int carreraId;

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
