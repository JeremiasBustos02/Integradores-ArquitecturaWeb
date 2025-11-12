package com.microservices.viajes.exception;

public class ViajeNotFoundException extends RuntimeException {
    public ViajeNotFoundException(){
        super("Viaje no encontrado");
    };
    public ViajeNotFoundException(String id) {
        super("Viaje no encontrado con ID: " + id);
    }
}

