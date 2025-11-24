package com.microservices.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDTO {

    @NotNull(message = "El email es un campo requerido.")
    @NotEmpty(message = "El email es un campo requerido.")
    @Schema(description = "Nombre de usuario", example = "admin@test.com")
    private String username;

    @NotNull(message = "La contraseña es un campo requerido.")
    @NotEmpty(message = "La contraseña es un campo requerido.")
    @Schema(description = "Contraseña", example = "admin123")
    private String password;
}

