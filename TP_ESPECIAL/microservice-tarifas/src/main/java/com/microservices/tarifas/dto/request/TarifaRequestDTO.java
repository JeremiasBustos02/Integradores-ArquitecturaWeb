package com.microservices.tarifas.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifaRequestDTO {
    
    @NotNull(message = "El monto base es obligatorio")
    @Positive(message = "El monto base debe ser positivo")
    private Double montoBase;
    
    @NotNull(message = "El monto extra es obligatorio")
    @Positive(message = "El monto extra debe ser positivo")
    private Double montoExtra;
    
    @NotNull(message = "La fecha de vigencia es obligatoria")
    private LocalDate fechaVigencia;
    
    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activa;
    
    private String descripcion;
}

