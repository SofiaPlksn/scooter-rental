package com.rutmiit.demo.api.endpoints;

import com.rutmiit.demo.api.dto.RentalRequest;
import com.rutmiit.demo.api.dto.RentalResponse;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "rentals", description = "API для аренды самокатов")
@RequestMapping("/api/rentals")
public interface RentalApi {

    @Operation(summary = "Получить аренду по ID")
    @ApiResponse(responseCode = "200", description = "Аренда найдена")
    @ApiResponse(responseCode = "404", description = "Аренда не найдена",
            content = @Content(schema = @Schema(implementation = com.rutmiit.demo.api.dto.StatusResponse.class)))
    @GetMapping("/{id}")
    EntityModel<RentalResponse> getRentalById(@PathVariable("id") Long id);

    @Operation(summary = "Получить список всех аренд (с фильтрацией по пользователю)")
    @ApiResponse(responseCode = "200", description = "Список аренд")
    @GetMapping
    PagedModel<EntityModel<RentalResponse>> getAllRentals(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "Начать аренду самоката")
    @ApiResponse(responseCode = "201", description = "Аренда начата")
    @ApiResponse(responseCode = "400", description = "Самокат недоступен или запрос невалиден",
            content = @Content(schema = @Schema(implementation = com.rutmiit.demo.api.dto.StatusResponse.class)))
    @ApiResponse(responseCode = "404", description = "Пользователь или самокат не найдены",
            content = @Content(schema = @Schema(implementation = com.rutmiit.demo.api.dto.StatusResponse.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<RentalResponse>> startRental(@Valid @RequestBody RentalRequest request);

    @Operation(summary = "Завершить аренду по ID")
    @ApiResponse(responseCode = "200", description = "Аренда завершена")
    @ApiResponse(responseCode = "404", description = "Аренда не найдена",
            content = @Content(schema = @Schema(implementation = com.rutmiit.demo.api.dto.StatusResponse.class)))
    @PostMapping("/{id}/finish")
    EntityModel<RentalResponse> finishRental(@PathVariable("id") Long id);
}