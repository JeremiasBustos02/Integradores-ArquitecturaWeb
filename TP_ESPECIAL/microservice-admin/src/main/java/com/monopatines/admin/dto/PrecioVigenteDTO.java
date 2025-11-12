package com.monopatines.admin.dto;
import lombok.Data; 
import java.time.OffsetDateTime;

@Data 
public class PrecioVigenteDTO { 
    private Long id; 
    private double precioKm; 
    private double precioMin; 
    private String moneda; 
    private OffsetDateTime vigenteDesde; 
    private OffsetDateTime vigenteHasta;
    private boolean activo; 
}
