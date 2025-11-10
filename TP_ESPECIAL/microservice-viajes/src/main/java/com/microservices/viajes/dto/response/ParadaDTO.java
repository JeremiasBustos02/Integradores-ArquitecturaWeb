package com.microservices.viajes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParadaDTO {
    private Long id;
    private String nombre;
    private Double latitud;
    private Double longitud;
}
