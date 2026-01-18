package edu.rutmiit.demo.events;

import java.io.Serializable;

public record ScooterDeletedEvent(
        Long scooterId
) implements Serializable {}


