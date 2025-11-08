package com.microservices.microservicemonopatin.exception;

public class MonopatinNotFoundException extends RuntimeException {
    public MonopatinNotFoundException(String message) {
        super(message);
    }
}
