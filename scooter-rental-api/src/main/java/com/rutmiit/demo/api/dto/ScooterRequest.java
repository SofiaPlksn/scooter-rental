package com.rutmiit.demo.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ScooterRequest(
        @NotBlank(message = "Модель самоката не может быть пустой")
        String model,
        @NotBlank(message = "Серийный номер обязателен")
        String serialNumber
) {}

