package com.rutmiit.demo.api.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateScooterRequest(
        @NotBlank(message = "Модель не может быть пустой")
        String model,
        @NotBlank(message = "Серийный номер не может быть пустым")
        String serialNumber
) {}
