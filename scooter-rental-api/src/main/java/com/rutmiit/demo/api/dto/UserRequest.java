package com.rutmiit.demo.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRequest(
        @NotBlank(message = "Имя пользователя не может быть пустым")
        String fullName,
        @NotBlank(message = "Номер телефона обязателен")
        @Pattern(regexp = "\\+?[0-9]{10,15}", message = "Некорректный формат номера телефона")
        String phone
) {}
