package com.microservices.tarifas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "precio_vigente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrecioVigente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double precioKm;

    @Column(nullable = false)
    private Double precioMin;

    @Column(nullable = false, length = 3)
    private String moneda;

    @Column(name = "vigente_desde", nullable = false)
    private OffsetDateTime vigenteDesde;

    @Column(name = "vigente_hasta")
    private OffsetDateTime vigenteHasta;

    @Column(nullable = false)
    private Boolean activo;
}

