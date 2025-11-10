package com.microservices.microservicemonopatin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "monopatines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Monopatin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double latitud;

    @Column(nullable = false)
    private double longitud;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoMonopatin estado;

    @Column(name = "km_totales", nullable = false)
    private double kmTotales = 0.0;

    @Column(name = "tiempo_uso_total", nullable = false)
    private long tiempoUsoTotal = 0L;

    @Column(name = "tiempo_pausas_totales", nullable = false)
    private long tiempoPausasTotales = 0L;

    @Column(name = "en_mantenimiento", nullable = false)
    private boolean enMantenimiento = false;

    @Column(name = "id_parada_actual")
    private long idParadaActual;
}
