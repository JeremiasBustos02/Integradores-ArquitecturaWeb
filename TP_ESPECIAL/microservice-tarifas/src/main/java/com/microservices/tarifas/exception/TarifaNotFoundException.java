package com.microservices.tarifas.exception;

public class TarifaNotFoundException extends RuntimeException {
    
    public TarifaNotFoundException(String message) {
        super(message);
    }
}

