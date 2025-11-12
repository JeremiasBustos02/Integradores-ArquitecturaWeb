package com.monopatines.admin.dto;
import lombok.Data; 
import java.math.BigDecimal;

@Data 
public class TotalFacturadoDTO { 
    private int anio; 
    private int mesDesde; 
    private int mesHasta; 
    private BigDecimal total; 
    private String moneda; 
}
