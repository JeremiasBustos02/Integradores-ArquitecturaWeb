package com.microservices.microservicemonopatin.dto;

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
    private Double kmTotales;
    private Long tiempoUsoTotal;
    private Boolean enMantenimiento;
    private Long idParadaActual;
}