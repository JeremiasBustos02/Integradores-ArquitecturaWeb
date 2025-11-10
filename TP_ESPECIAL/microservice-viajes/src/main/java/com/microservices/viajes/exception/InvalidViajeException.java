package com.microservices.viajes.exception;

public class InvalidViajeException extends RuntimeException {
    public InvalidViajeException(String message) {
        super(message);
    }
}
