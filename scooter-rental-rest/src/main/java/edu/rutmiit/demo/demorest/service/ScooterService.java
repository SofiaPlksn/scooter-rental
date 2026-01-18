package edu.rutmiit.demo.demorest.service;


import com.rutmiit.demo.api.dto.*;
import com.rutmiit.demo.api.exception.ResourceNotFoundException;
import com.rutmiit.demo.api.exception.ScooterAlreadyExistsException;
import edu.rutmiit.demo.demorest.config.RabbitMQConfig;
import edu.rutmiit.demo.demorest.storage.InMemoryStorage;
import edu.rutmiit.demo.events.ScooterCreatedEvent;
import edu.rutmiit.demo.events.ScooterDeletedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScooterService {

    private final InMemoryStorage storage;
    private final RabbitTemplate rabbitTemplate;

    public ScooterService(InMemoryStorage storage, RabbitTemplate rabbitTemplate) {
        this.storage = storage;
        this.rabbitTemplate = rabbitTemplate;
    }

    public PagedResponse<ScooterResponse> getAllScooters(Boolean available, int page, int size) {
        List<ScooterResponse> all = storage.scooters.values()
                .stream()
                .filter(s -> available == null || s.isAvailable() == available)
                .collect(Collectors.toList());

        int from = Math.min(page * size, all.size());
        int to = Math.min(from + size, all.size());
        List<ScooterResponse> content = all.subList(from, to);

        int totalPages = (int) Math.ceil((double) all.size() / size);

        return new PagedResponse<>(content, page, size, all.size(), totalPages, page >= totalPages - 1);
    }

    public ScooterResponse getScooterById(Long id) {
        ScooterResponse scooter = storage.scooters.get(id);
        if (scooter == null) {
            throw new ResourceNotFoundException("Scooter", id);
        }
        return scooter;
    }

    public ScooterResponse createScooter(ScooterRequest request) {
        // Проверяем уникальность серийного номера
        boolean exists = storage.scooters.values().stream()
                .anyMatch(s -> s.getSerialNumber().equalsIgnoreCase(request.serialNumber()));
        if (exists) {
            throw new ScooterAlreadyExistsException(request.serialNumber());
        }

        long id = storage.scooterSequence.incrementAndGet();
        LocalDateTime createdAt = LocalDateTime.now();

        ScooterResponse scooter = new ScooterResponse(
                id,
                request.model(),
                request.serialNumber(),
                true,
                createdAt
        );
        storage.scooters.put(id, scooter);

        // Публикуем событие создания самоката
        ScooterCreatedEvent event = new ScooterCreatedEvent(
                id,
                request.model(),
                request.serialNumber()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY_SCOOTER_CREATED,
                event
        );
        return scooter;
    }

    public ScooterResponse updateScooter(Long id, UpdateScooterRequest request) {
        ScooterResponse existing = storage.scooters.get(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Scooter", id);
        }

        ScooterResponse updated = new ScooterResponse(
                id,
                request.model(),
                request.serialNumber(),
                existing.isAvailable(),
                existing.getCreatedAt()
        );
        storage.scooters.put(id, updated);
        return updated;
    }

    public void deleteScooter(Long id) {
        ScooterResponse scooter = storage.scooters.get(id);
        if (scooter == null) {
            throw new ResourceNotFoundException("Scooter", id);
        }

        storage.scooters.remove(id);

        // Публикуем событие удаления самоката
        ScooterDeletedEvent event = new ScooterDeletedEvent(id);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY_SCOOTER_DELETED,
                event
        );
    }
}