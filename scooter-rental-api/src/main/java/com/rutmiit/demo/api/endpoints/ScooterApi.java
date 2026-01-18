package com.rutmiit.demo.api.endpoints;

import com.rutmiit.demo.api.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "scooters", description = "API для работы с самокатами")
@RequestMapping("/api/scooters")
public interface ScooterApi {

    @Operation(summary = "Получить самокат по ID")
    @ApiResponse(responseCode = "200", description = "Самокат найден")
    @ApiResponse(responseCode = "404", description = "Самокат не найден",
            content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @GetMapping("/{id}")
    EntityModel<ScooterResponse> getScooterById(@PathVariable("id") Long id);

    @Operation(summary = "Получить список самокатов с пагинацией и фильтрацией по доступности")
    @ApiResponse(responseCode = "200", description = "Список самокатов")
    @GetMapping
    PagedModel<EntityModel<ScooterResponse>> getAllScooters(
            @Parameter(description = "Фильтр по доступности (true/false)")
            @RequestParam(required = false) Boolean available,
            @Parameter(description = "Номер страницы (0..N)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы")
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "Создать новый самокат")
    @ApiResponse(responseCode = "201", description = "Самокат успешно создан")
    @ApiResponse(responseCode = "400", description = "Невалидный запрос",
            content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<ScooterResponse>> createScooter(@Valid @RequestBody ScooterRequest request);

    @Operation(summary = "Обновить данные самоката по ID")
    @ApiResponse(responseCode = "200", description = "Самокат обновлен")
    @ApiResponse(responseCode = "404", description = "Самокат не найден",
            content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PutMapping("/{id}")
    EntityModel<ScooterResponse> updateScooter(@PathVariable Long id, @Valid @RequestBody UpdateScooterRequest request);

    @Operation(summary = "Удалить самокат")
    @ApiResponse(responseCode = "204", description = "Самокат удален")
    @ApiResponse(responseCode = "404", description = "Самокат не найден")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteScooter(@PathVariable Long id);
}