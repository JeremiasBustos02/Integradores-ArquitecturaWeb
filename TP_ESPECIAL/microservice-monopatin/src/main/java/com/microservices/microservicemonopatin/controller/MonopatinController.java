package com.microservices.microservicemonopatin.controller;

import com.microservices.microservicemonopatin.dto.MonopatinDTO;
import com.microservices.microservicemonopatin.dto.MonopatinRequestDTO;
import com.microservices.microservicemonopatin.service.MonopatinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/monopatines")
@RequiredArgsConstructor
public class MonopatinController {

    private final MonopatinService monopatinService;

    // CRUD básico
    @PostMapping
    public ResponseEntity<MonopatinDTO> crear(@Valid @RequestBody MonopatinRequestDTO monopatinDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(monopatinService.crear(monopatinDTO));
    }
}
