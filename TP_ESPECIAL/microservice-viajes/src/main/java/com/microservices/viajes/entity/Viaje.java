package com.microservices.viajes.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Document(collection = "viajes")
public class Viaje {
    @Id
    private String id;
    @Field//inicio del viaje
    private LocalDateTime horaInicio;
    @Field//fin viaje
    private LocalDateTime horaFin;
    @Field
    private double distanciaRecorrida;
    @Field
    private Long paradaOrigenId;
    @Field
    private Long paradaDestinoId;
    @Field
    private Long monopatinId;
    @Field
    private Long tarifaId;
    @Field
    private Long usuarioId;
    @Field
    boolean pausaExtensa;
    @Field
    private List<Pausa> pausas;

    public Viaje(Long monopatinId, Long paradaOrigenId, Long usuarioId, Long tarifaId) {
        this.monopatinId = monopatinId;
        this.paradaOrigenId = paradaOrigenId;
        this.usuarioId = usuarioId;
        this.tarifaId = tarifaId;
    }
}
