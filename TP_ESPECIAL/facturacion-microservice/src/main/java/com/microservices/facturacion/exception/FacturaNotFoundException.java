package com.microservices.facturacion.exception;

public class FacturaNotFoundException extends RuntimeException {
    
    public FacturaNotFoundException(String message) {
        super(message);
    }
}

