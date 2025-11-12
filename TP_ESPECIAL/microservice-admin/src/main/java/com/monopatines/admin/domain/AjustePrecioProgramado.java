package com.monopatines.admin.domain;
import jakarta.persistence.*; 
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "ajuste_precio_programado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AjustePrecioProgramado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "precio_km", nullable = false)
    private Double precioKm;

    @Column(name = "precio_min", nullable = false)
    private Double precioMin;

    @Column(name = "extra_pausa_multiplicador", nullable = false)
    private Double extraPausaMultiplicador;

    @Column(name = "efectiva_desde", nullable = false)
    private OffsetDateTime efectivaDesde;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoAjuste estado = EstadoAjuste.PENDIENTE;
}