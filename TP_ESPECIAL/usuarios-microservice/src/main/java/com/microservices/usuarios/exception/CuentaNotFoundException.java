package com.microservices.usuarios.exception;

public class CuentaNotFoundException extends RuntimeException {
    public CuentaNotFoundException(String message) {
        super(message);
    }
    
    public CuentaNotFoundException(Long id) {
        super("Cuenta no encontrada con ID: " + id);
    }
}
