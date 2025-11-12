package com.monopatines.admin.domain;
import jakarta.persistence.*; 
import lombok.Data; 
import java.time.OffsetDateTime;

@Entity 
@Data 
public class PrecioVigente {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private double precioKm; 
    private double precioMin; 
    private String moneda;
    private OffsetDateTime vigenteDesde; 
    private OffsetDateTime vigenteHasta; 
    private boolean activo;
}