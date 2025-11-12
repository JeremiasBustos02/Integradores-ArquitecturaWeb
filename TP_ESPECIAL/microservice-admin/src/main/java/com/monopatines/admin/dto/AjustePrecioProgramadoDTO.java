package com.monopatines.admin.dto;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data; 
import java.time.OffsetDateTime;

@Data 
public class AjustePrecioProgramadoDTO { 
    private Long id;

    @NotNull(message = "El precio por kilómetro es obligatorio")
    @Positive(message = "El precio por kilómetro debe ser mayor a cero")
    private Double precioKm;

    @NotNull(message = "El precio por minuto es obligatorio")
    @Positive(message = "El precio por minuto debe ser mayor a cero")
    private Double precioMin;

    @NotNull(message = "El multiplicador de pausa extra es obligatorio")
    @Positive(message = "El multiplicador debe ser mayor a cero")
    private Double extraPausaMultiplicador;

    @NotNull(message = "La fecha efectiva es obligatoria")
    @Future(message = "La fecha efectiva debe ser futura")
    private OffsetDateTime efectivaDesde;

    private String estado; 
}
