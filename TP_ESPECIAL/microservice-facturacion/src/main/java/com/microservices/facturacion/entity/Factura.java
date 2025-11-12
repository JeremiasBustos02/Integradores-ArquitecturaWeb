package com.microservices.facturacion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "facturas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_factura", unique = true, nullable = false)
    private String numeroFactura;

    @Column(name = "cuenta_id", nullable = false)
    private Long cuentaId;

    @Column(name = "viaje_id")
    private String viajeId;

    @Column(name = "monto_total", nullable = false)
    private Double montoTotal;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoFactura estado;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "periodo_mes")
    private Integer periodoMes;

    @Column(name = "periodo_anio")
    private Integer periodoAnio;

    @Column(name = "tipo_cuenta")
    private String tipoCuenta;

    public Factura(String numeroFactura, Long cuentaId, String viajeId, Double montoTotal,
                   LocalDateTime fechaEmision, LocalDate fechaVencimiento, EstadoFactura estado,
                   String descripcion, Integer periodoMes, Integer periodoAnio, String tipoCuenta) {
        this.numeroFactura = numeroFactura;
        this.cuentaId = cuentaId;
        this.viajeId = viajeId;
        this.montoTotal = montoTotal;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.descripcion = descripcion;
        this.periodoMes = periodoMes;
        this.periodoAnio = periodoAnio;
        this.tipoCuenta = tipoCuenta;
    }
}