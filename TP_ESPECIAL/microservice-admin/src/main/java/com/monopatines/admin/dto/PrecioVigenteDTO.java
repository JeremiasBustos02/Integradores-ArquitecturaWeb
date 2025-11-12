package com.monopatines.admin.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data; 
import java.time.OffsetDateTime;

@Data 
public class PrecioVigenteDTO { 
    private Long id;

    @NotNull(message = "El precio por kilómetro es obligatorio")
    @Positive(message = "El precio por kilómetro debe ser mayor a cero")
    private Double precioKm;

    @NotNull(message = "El precio por minuto es obligatorio")
    @Positive(message = "El precio por minuto debe ser mayor a cero")
    private Double precioMin;

    @NotBlank(message = "La moneda es obligatoria")
    private String moneda;

    private OffsetDateTime vigenteDesde; 
    private OffsetDateTime vigenteHasta;
    private boolean activo; 
}
