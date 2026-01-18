package com.rutmiit.demo.api.endpoints;

import com.rutmiit.demo.api.dto.StatusResponse;
import com.rutmiit.demo.api.dto.UserRequest;
import com.rutmiit.demo.api.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "users", description = "API для управления пользователями сервиса аренды самокатов")
@RequestMapping("/api/users")
public interface UserApi {

    @Operation(summary = "Получить список всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей")
    @GetMapping
    CollectionModel<EntityModel<UserResponse>> getAllUsers();

    @Operation(summary = "Получить пользователя по ID")
    @ApiResponse(responseCode = "200", description = "Пользователь найден")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден",
            content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @GetMapping("/{id}")
    EntityModel<UserResponse> getUserById(@PathVariable Long id);

    @Operation(summary = "Создать нового пользователя")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан")
    @ApiResponse(responseCode = "400", description = "Невалидный запрос",
            content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<UserResponse>> createUser(@Valid @RequestBody UserRequest request);

    @Operation(summary = "Обновить данные пользователя")
    @ApiResponse(responseCode = "200", description = "Пользователь обновлен")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден",
            content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PutMapping("/{id}")
    EntityModel<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request);

    @Operation(summary = "Удалить пользователя")
    @ApiResponse(responseCode = "204", description = "Пользователь удален")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable Long id);
}