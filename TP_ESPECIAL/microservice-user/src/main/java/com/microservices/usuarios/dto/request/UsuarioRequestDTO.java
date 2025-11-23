package com.microservices.usuarios.dto.request;

import com.microservices.usuarios.entity.Rol;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {
    @Schema(description = "Nombre", example = "Tomas")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @Schema(description = "Apellido", example = "Benavidez")
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    @Schema(description = "Celular (10 a 15 dígitos)", example = "2494500123")
    @NotBlank(message = "El celular es obligatorio")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "El celular debe tener entre 10 y 15 dígitos")
    private String celular;
    @Schema(description = "Email", example = "n21@gmail.com")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;
    @Schema(description = "Contraseña", example = "1234")
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @Schema(description = "Rol", example = "ADMIN")
    private Rol rol = Rol.USUARIO;
}
