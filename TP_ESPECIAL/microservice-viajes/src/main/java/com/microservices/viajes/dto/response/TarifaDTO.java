package com.microservices.viajes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TarifaDTO {
    private Long id;
    private Double montoBase;
    private Double montoExtra;
    private LocalDate fechaVigencia;
    private Boolean activa;
    private String descripcion;
}
