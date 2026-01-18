package edu.rutmiit.demo.demorest.storage;

import com.rutmiit.demo.api.dto.RentalResponse;
import com.rutmiit.demo.api.dto.ScooterResponse;
import com.rutmiit.demo.api.dto.UserResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryStorage {

    public final Map<Long, UserResponse> users = new ConcurrentHashMap<>();
    public final Map<Long, ScooterResponse> scooters = new ConcurrentHashMap<>();
    public final Map<Long, RentalResponse> rentals = new ConcurrentHashMap<>();

    public final AtomicLong userSequence = new AtomicLong(0);
    public final AtomicLong scooterSequence = new AtomicLong(0);
    public final AtomicLong rentalSequence = new AtomicLong(0);

    @PostConstruct
    public void init() {
        UserResponse user1 = new UserResponse(userSequence.incrementAndGet(), "Иван Петров", "+79998887766");
        UserResponse user2 = new UserResponse(userSequence.incrementAndGet(), "Анна Смирнова", "+79997776655");
        users.put(user1.getId(), user1);
        users.put(user2.getId(), user2);

        ScooterResponse scooter1 = new ScooterResponse(scooterSequence.incrementAndGet(), "Xiaomi M365", "SN123456", true, LocalDateTime.now());
        ScooterResponse scooter2 = new ScooterResponse(scooterSequence.incrementAndGet(), "Ninebot Max", "SN654321", true, LocalDateTime.now());
        scooters.put(scooter1.getId(), scooter1);
        scooters.put(scooter2.getId(), scooter2);
    }
}