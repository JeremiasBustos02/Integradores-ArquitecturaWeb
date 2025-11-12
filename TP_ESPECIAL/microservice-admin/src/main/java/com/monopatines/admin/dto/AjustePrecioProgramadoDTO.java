package com.monopatines.admin.dto;
import lombok.Data; 
import java.time.OffsetDateTime;
@Data 
public class AjustePrecioProgramadoDTO { 
    private Long id; 
    private double precioKm; 
    private double precioMin; 
    private double extraPausaMultiplicador; 
    private OffsetDateTime efectivaDesde; 
    private String estado; 
}
