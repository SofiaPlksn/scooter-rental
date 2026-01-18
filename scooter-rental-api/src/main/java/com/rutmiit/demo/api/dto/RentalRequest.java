package com.rutmiit.demo.api.dto;

import jakarta.validation.constraints.NotNull;

public record RentalRequest(
        @NotNull(message = "ID пользователя обязателен")
        Long userId,
        @NotNull(message = "ID самоката обязателен")
        Long scooterId
) {}
