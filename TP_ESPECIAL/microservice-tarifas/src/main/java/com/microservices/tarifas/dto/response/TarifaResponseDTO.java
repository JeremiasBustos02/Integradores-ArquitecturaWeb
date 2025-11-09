package com.microservices.tarifas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifaResponseDTO {
    
    private Long id;
    private Double montoBase;
    private Double montoExtra;
    private LocalDate fechaVigencia;
    private Boolean activa;
    private String descripcion;
}

