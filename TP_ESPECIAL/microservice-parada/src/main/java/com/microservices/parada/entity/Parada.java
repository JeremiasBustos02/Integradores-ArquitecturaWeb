package com.microservices.parada.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "paradas")
public class Parada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false)
    private String nombre;
    @NotNull(message = "La latitud es obligatoria")
    @Column(nullable = false)
    private Double latitud;
    @NotNull(message = "La longitud es obligatoria")
    @Column(nullable = false)
    private Double longitud;
}
