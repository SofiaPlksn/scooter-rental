package edu.rutmiit.demo.events;

import java.io.Serializable;

public record UserRatedEvent(
        Long userId,
        Integer ratingScore,
        String verdict
) implements Serializable {}


