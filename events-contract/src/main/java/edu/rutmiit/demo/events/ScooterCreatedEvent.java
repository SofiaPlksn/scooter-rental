package edu.rutmiit.demo.events;

import java.io.Serializable;

public record ScooterCreatedEvent(
        Long scooterId,
        String model,
        String serialNumber
) implements Serializable {}



