package com.microservices.microservicemonopatin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteKilometrosDTO {
    private Long id;
    private Double kmTotales;
    private Long tiempoUsoTotal;
    private Long tiempoPausasTotal;
    private Long tiempoUsoSinPausas;
}