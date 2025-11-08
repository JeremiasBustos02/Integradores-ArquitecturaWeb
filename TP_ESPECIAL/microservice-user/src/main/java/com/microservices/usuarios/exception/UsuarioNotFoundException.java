package com.microservices.usuarios.exception;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(String message) {
        super(message);
    }
    
    public UsuarioNotFoundException(Long id) {
        super("Usuario no encontrado con ID: " + id);
    }
}
