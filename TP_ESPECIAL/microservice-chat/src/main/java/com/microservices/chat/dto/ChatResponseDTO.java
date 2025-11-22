package com.microservices.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta del chat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseDTO {
    
    private boolean exito;
    private String mensaje;
    private Object datos;
    
    public ChatResponseDTO(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.datos = null;
    }
}

