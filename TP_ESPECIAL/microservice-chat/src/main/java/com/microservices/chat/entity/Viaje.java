package com.microservices.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "viajes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Viaje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;
    
    @Column(name = "monopatin_id", nullable = false)
    private Long monopatinId;
    
    @Column(name = "parada_origen_id")
    private Long paradaOrigenId;
    
    @Column(name = "parada_destino_id")
    private Long paradaDestinoId;
    
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;
    
    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;
    
    @Column(name = "distancia_km")
    private Double distanciaKm;
    
    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;
    
    @Column(name = "costo_total")
    private Double costoTotal;
    
    @Column(name = "estado")
    private String estado;
}

