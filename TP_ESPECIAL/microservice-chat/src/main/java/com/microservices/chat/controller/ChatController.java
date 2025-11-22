package com.microservices.chat.controller;

import com.microservices.chat.dto.ChatRequestDTO;
import com.microservices.chat.dto.ChatResponseDTO;
import com.microservices.chat.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    /**
     * Endpoint principal del chat.
     * Recibe un mensaje del usuario y devuelve la respuesta del asistente.
     * 
     * POST /api/chat
     * Body: { "usuarioId": 1, "mensaje": "¿Cuáles son las paradas disponibles?" }
     */
    @PostMapping
    public ResponseEntity<ChatResponseDTO> chat(@Valid @RequestBody ChatRequestDTO request) {
        ChatResponseDTO response = chatService.procesarMensaje(request.getUsuarioId(), request.getMensaje());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de health check
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Chat service is running");
    }
}

