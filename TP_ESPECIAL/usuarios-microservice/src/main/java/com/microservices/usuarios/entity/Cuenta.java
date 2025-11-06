package com.microservices.usuarios.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cuentas")
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "id_cuenta_mercado_pago", unique = true)
    private Long idCuentaMercadoPago;

    @Column(nullable = false, name = "fecha_alta", updatable = false)
    private LocalDateTime fechaAlta;

    @Column(name = "estado_cuenta", nullable = false, columnDefinition = "boolean default true")
    @Builder.Default
    private Boolean estadoCuenta = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuenta", nullable = false, columnDefinition = "varchar(20) default 'BASICA'")
    @Builder.Default
    private TipoCuenta tipoCuenta = TipoCuenta.BASICA;

    @Column(name = "saldo", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal saldo = BigDecimal.ZERO;

    @Column(name = "kilometros_recorridos_mes", nullable = false)
    @Builder.Default
    private Double kilometrosRecorridosMes = 0.0;

    @Column(name = "fecha_renovacion_cupo")
    private LocalDateTime fechaRenovacionCupo;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.MERGE
    })
    @JoinTable(
            name = "cuentas_usuarios",
            joinColumns = @JoinColumn(name = "cuenta_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    @JsonManagedReference
    @Builder.Default
    private Set<Usuario> usuarios = new HashSet<>();

    @PrePersist
    private void prePersist() {
        this.fechaAlta = LocalDateTime.now();
        if (this.tipoCuenta == TipoCuenta.PREMIUM) {
            this.fechaRenovacionCupo = LocalDateTime.now().plusMonths(1);
        }
    }

    public void removeUsuario(Usuario usuario) {
        this.usuarios.remove(usuario);
        usuario.getCuentas().remove(this);
    }

    public void addUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
        usuario.getCuentas().add(this);
    }

    public void renovarCupoPremium() {
        if (this.tipoCuenta == TipoCuenta.PREMIUM) {
            this.kilometrosRecorridosMes = 0.0;
            this.fechaRenovacionCupo = LocalDateTime.now().plusMonths(1);
        }
    }

    public boolean tieneCupoPremiumDisponible() {
        return this.tipoCuenta == TipoCuenta.PREMIUM && this.kilometrosRecorridosMes < 100.0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cuenta cuenta = (Cuenta) o;
        return Objects.equals(getId(), cuenta.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
