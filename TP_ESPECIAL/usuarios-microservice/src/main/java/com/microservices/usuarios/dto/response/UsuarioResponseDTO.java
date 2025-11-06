package com.microservices.usuarios.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String celular;
    private String email;
    private LocalDateTime fechaAlta;
    private Set<CuentaBasicResponseDTO> cuentas;
}
