package com.rutmiit.demo.api.exception;

public class ScooterUnavailableException extends RuntimeException {
    public ScooterUnavailableException(Long scooterId) {
        super(String.format("Scooter with id=%s is currently unavailable for rental", scooterId));
    }
}
