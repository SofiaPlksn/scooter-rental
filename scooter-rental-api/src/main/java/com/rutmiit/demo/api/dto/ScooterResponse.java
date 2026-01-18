package com.rutmiit.demo.api.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.Objects;

@Relation(collectionRelation = "scooters", itemRelation = "scooter")
public class ScooterResponse extends RepresentationModel<ScooterResponse> {
    private final Long id;
    private final String model;
    private final String serialNumber;
    private final boolean available;
    private final LocalDateTime createdAt;

    public ScooterResponse(Long id, String model, String serialNumber,
                           boolean available, LocalDateTime createdAt) {
        this.id = id;
        this.model = model;
        this.serialNumber = serialNumber;
        this.available = available;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public boolean isAvailable() {
        return available;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ScooterResponse that = (ScooterResponse) o;
        return available == that.available &&
                Objects.equals(id, that.id) &&
                Objects.equals(model, that.model) &&
                Objects.equals(serialNumber, that.serialNumber) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, model, serialNumber, available, createdAt);
    }
}