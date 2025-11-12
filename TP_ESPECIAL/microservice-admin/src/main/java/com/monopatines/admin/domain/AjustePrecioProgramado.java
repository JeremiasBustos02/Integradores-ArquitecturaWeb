package com.monopatines.admin.domain;
import jakarta.persistence.*; 
import lombok.Data; 
import java.time.OffsetDateTime;

@Entity
@Data 
public class AjustePrecioProgramado {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private double precioKm; private double precioMin; private double extraPausaMultiplicador;
    private OffsetDateTime efectivaDesde; @Enumerated(EnumType.STRING) private EstadoAjuste estado = EstadoAjuste.PENDIENTE;
}