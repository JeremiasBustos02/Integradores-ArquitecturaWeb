package com.microservices.chat.controller;

import com.microservices.chat.dto.ChatRequestDTO;
import com.microservices.chat.dto.ChatResponseDTO;
import com.microservices.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chatbot del servicio de monopatines", description = "Endpoints para hablar con IA")
public class ChatController {

    @Autowired
    private ChatService chatService;

    /**
     * Endpoint principal del chat.
     * Recibe un mensaje del usuario y devuelve la respuesta del asistente.
     * <p>
     * POST /api/chat
     * Body: { "usuarioId": 1, "mensaje": "¿Cuáles son las paradas disponibles?" }
     */
    @Operation(summary = "Pedir que el asistente te responda", description = "Hablar con el asistente IA")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Respuesta generada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en el formato del mensaje"),
            @ApiResponse(responseCode = "500", description = "Error interno del servicio de IA")})
    @PostMapping
    public ResponseEntity<ChatResponseDTO> chat(@Valid @RequestBody ChatRequestDTO request) {
        ChatResponseDTO response = chatService.procesarMensaje(request.getUsuarioId(), request.getMensaje());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de health check
     */
    @Operation(summary = "Verificar el estado del servicio", description = "Responde con el estado en el que se encuentra el servicio")
    @ApiResponse(responseCode = "200", description = "Servicio operativo")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Chat service is running");
    }
}

