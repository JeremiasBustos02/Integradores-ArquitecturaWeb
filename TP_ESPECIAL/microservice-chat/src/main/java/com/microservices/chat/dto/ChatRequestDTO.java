package com.microservices.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la petición de chat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestDTO {

    @NotNull(message = "El ID de usuario es obligatorio")
    @Schema(description = "ID del usuario que envía el mensaje", example = "1")
    private Long usuarioId;

    @Schema(description = "Texto del mensaje para la IA", example = "¿Cuáles son las paradas disponibles?")
    @NotBlank(message = "El mensaje no puede estar vacío")
    private String mensaje;
}

