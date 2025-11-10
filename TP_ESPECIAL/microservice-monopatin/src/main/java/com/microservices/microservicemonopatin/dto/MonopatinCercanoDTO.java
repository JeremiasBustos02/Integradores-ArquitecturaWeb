package com.microservices.microservicemonopatin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonopatinCercanoDTO {
    private Long id;
    private Double latitud;
    private Double longitud;
    private Double distancia; // en km
}
