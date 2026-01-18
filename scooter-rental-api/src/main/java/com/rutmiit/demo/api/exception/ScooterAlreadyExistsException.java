package com.rutmiit.demo.api.exception;

public class ScooterAlreadyExistsException extends RuntimeException {
    public ScooterAlreadyExistsException(String serialNumber) {
        super("Scooter with serial number=" + serialNumber + " already exists");
    }
}