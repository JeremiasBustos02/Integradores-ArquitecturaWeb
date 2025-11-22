package com.microservices.chat.dto;

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
    private Long usuarioId;
    
    @NotBlank(message = "El mensaje no puede estar vacío")
    private String mensaje;
}

