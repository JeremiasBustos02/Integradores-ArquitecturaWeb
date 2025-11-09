package com.microservices.tarifas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tarifas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "monto_base", nullable = false)
    private Double montoBase;
    
    @Column(name = "monto_extra", nullable = false)
    private Double montoExtra;
    
    @Column(name = "fecha_vigencia", nullable = false)
    private LocalDate fechaVigencia;
    
    @Column(nullable = false)
    private Boolean activa;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    public Tarifa(Double montoBase, Double montoExtra, LocalDate fechaVigencia, Boolean activa, String descripcion) {
        this.montoBase = montoBase;
        this.montoExtra = montoExtra;
        this.fechaVigencia = fechaVigencia;
        this.activa = activa;
        this.descripcion = descripcion;
    }
}

