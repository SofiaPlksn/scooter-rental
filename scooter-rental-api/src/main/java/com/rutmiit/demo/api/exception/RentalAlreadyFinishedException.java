package com.rutmiit.demo.api.exception;

public class RentalAlreadyFinishedException extends RuntimeException {
    public RentalAlreadyFinishedException(Long rentalId) {
        super(String.format("Rental with id=%s has already been finished", rentalId));
    }
}
