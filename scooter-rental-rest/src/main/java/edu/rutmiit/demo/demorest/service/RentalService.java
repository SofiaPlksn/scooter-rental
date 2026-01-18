package edu.rutmiit.demo.demorest.service;

import com.rutmiit.demo.api.dto.*;
import com.rutmiit.demo.api.exception.RentalAlreadyFinishedException;
import com.rutmiit.demo.api.exception.ResourceNotFoundException;
import com.rutmiit.demo.api.exception.ScooterUnavailableException;
import edu.rutmiit.demo.demorest.storage.InMemoryStorage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalService {

    private final InMemoryStorage storage;

    public RentalService(InMemoryStorage storage) {
        this.storage = storage;
    }

    public PagedResponse<RentalResponse> getAllRentals(Long userId, int page, int size) {
        List<RentalResponse> all = storage.rentals.values().stream()
                .filter(r -> userId == null || r.getUser().getId().equals(userId))
                .collect(Collectors.toList());

        int from = Math.min(page * size, all.size());
        int to = Math.min(from + size, all.size());
        List<RentalResponse> content = all.subList(from, to);

        int totalPages = (int) Math.ceil((double) all.size() / size);

        return new PagedResponse<>(content, page, size, all.size(), totalPages, page >= totalPages - 1);
    }

    public RentalResponse getRentalById(Long id) {
        RentalResponse rental = storage.rentals.get(id);
        if (rental == null) {
            throw new ResourceNotFoundException("Rental", id);
        }
        return rental;
    }

    public RentalResponse startRental(RentalRequest request) {
        UserResponse user = storage.users.get(request.userId());
        if (user == null) {
            throw new ResourceNotFoundException("User", request.userId());
        }

        ScooterResponse scooter = storage.scooters.get(request.scooterId());
        if (scooter == null) {
            throw new ResourceNotFoundException("Scooter", request.scooterId());
        }

        if (!scooter.isAvailable()) {
            throw new ScooterUnavailableException(request.scooterId());
        }

        ScooterResponse updatedScooter = new ScooterResponse(
                scooter.getId(),
                scooter.getModel(),
                scooter.getSerialNumber(),
                false,
                scooter.getCreatedAt()
        );
        storage.scooters.put(scooter.getId(), updatedScooter);

        long rentalId = storage.rentalSequence.incrementAndGet();
        RentalResponse rental = new RentalResponse(
                rentalId,
                user,
                updatedScooter,
                LocalDateTime.now(),
                null
        );

        storage.rentals.put(rentalId, rental);
        return rental;
    }

    public RentalResponse finishRental(Long id) {
        RentalResponse rental = storage.rentals.get(id);
        if (rental == null) {
            throw new ResourceNotFoundException("Rental", id);
        }

        if (rental.getEndTime() != null) {
            throw new RentalAlreadyFinishedException(id);
        }

        ScooterResponse scooter = rental.getScooter();
        ScooterResponse updatedScooter = new ScooterResponse(
                scooter.getId(),
                scooter.getModel(),
                scooter.getSerialNumber(),
                true,
                scooter.getCreatedAt()
        );
        storage.scooters.put(scooter.getId(), updatedScooter);

        RentalResponse finished = new RentalResponse(
                rental.getId(),
                rental.getUser(),
                updatedScooter,
                rental.getStartTime(),
                LocalDateTime.now()
        );

        storage.rentals.put(id, finished);
        return finished;
    }
}