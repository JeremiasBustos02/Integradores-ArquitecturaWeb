package com.microservices.viajes.exception;

import reactor.core.publisher.Mono;

public class MonopatinNotFound extends RuntimeException {
    public MonopatinNotFound(String message) {
        super(message);
    }

    public MonopatinNotFound(Long id) {
        super("Monopatín no encontrado con ID: " + id);
    }
}
