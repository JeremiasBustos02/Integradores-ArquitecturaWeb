package com.microservices.microserviceiaservice.controller;

import com.microservices.microserviceiaservice.service.IAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ia")
public class IAController {
    @Autowired
    private IAService service;


    @PostMapping(value = "/prompt", produces = "application/json") // recibe un prompt ocmo json
    public ResponseEntity<?> procesarPrompt(@RequestBody String prompt) {
        try {
            return service.procesarPrompt(prompt);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el prompt: " + e.getMessage());
        }
    }
}
