package com.microservices.viajes.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor

@Document(collection = "pausa")
public class Pausa {
    @Field
    private LocalDateTime tiempoInicio;
    @Field
    private LocalDateTime tiempoFin;
}
