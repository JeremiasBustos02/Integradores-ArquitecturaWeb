package com.microservices.viajes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteUsoMonopatinDTO {
    private Long monopatinId;
    private Double totalKilometros;
    private Long totalMinutos;
    private Integer cantidadViajes;
}