package com.microservices.usuarios.dto.response;

import com.microservices.usuarios.entity.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioAuthResponseDTO {
    private Long id;
    private String email;
    private String password;
    private Rol rol;
}

