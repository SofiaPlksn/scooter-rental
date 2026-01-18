package com.rutmiit.demo.api.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.Objects;

@Relation(collectionRelation = "rentals", itemRelation = "rental")
public class RentalResponse extends RepresentationModel<RentalResponse> {
    private final Long id;
    private final UserResponse user;
    private final ScooterResponse scooter;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public RentalResponse(Long id, UserResponse user, ScooterResponse scooter,
                          LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.user = user;
        this.scooter = scooter;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public UserResponse getUser() {
        return user;
    }

    public ScooterResponse getScooter() {
        return scooter;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RentalResponse that = (RentalResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(user, that.user) &&
                Objects.equals(scooter, that.scooter) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, user, scooter, startTime, endTime);
    }
}