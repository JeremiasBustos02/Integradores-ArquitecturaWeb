package com.microservices.viajes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonopatinDTO {
    private Long id;
    private Double latitud;
    private Double longitud;
    private String estado;
}
