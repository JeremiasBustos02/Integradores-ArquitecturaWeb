package com.microservices.usuarios.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String celular;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "fecha_alta", nullable = false, updatable = false)
    private LocalDateTime fechaAlta;

    @ManyToMany(mappedBy = "usuarios", fetch = FetchType.LAZY)
    @JsonBackReference
    @Builder.Default
    private Set<Cuenta> cuentas = new HashSet<>();
    
    @PrePersist
    private void prePersist() {
        this.fechaAlta = LocalDateTime.now();
    }

    public void removeCuenta(Cuenta cuenta) {
        this.cuentas.remove(cuenta);
        cuenta.getUsuarios().remove(this);
    }

    public void addCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
        cuenta.getUsuarios().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(getId(), usuario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
