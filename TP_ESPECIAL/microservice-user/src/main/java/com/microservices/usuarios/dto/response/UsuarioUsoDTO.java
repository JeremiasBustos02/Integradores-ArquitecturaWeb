package com.microservices.usuarios.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUsoDTO {
    private Long usuarioId;
    private String nombre;
    private String apellido;
    private String email;
    private String tipoCuenta;
    private Integer cantidadViajes;
    private Double totalKilometros;
    private Long totalMinutos;
}

