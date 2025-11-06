package com.microservices.usuarios.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioBasicResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String celular;
    private String email;
    private LocalDateTime fechaAlta;
}
