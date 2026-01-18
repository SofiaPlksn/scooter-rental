package grpc.demo;

import grpc.demo.handler.ScooterNotificationHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final ScooterNotificationHandler notificationHandler;

    public NotificationController(ScooterNotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }

    @PostMapping("/broadcast")
    public ResponseEntity<Map<String, Object>> broadcast(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        int sent = notificationHandler.broadcast(message);

        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "sentTo", sent,
                "message", message
        ));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(Map.of(
                "activeConnections", notificationHandler.getActiveConnections()
        ));
    }

    @PostMapping("/test/scooter-created")
    public ResponseEntity<Map<String, Object>> testScooterCreated() {
        notificationHandler.sendScooterCreatedNotification(
                999L,
                "Test Scooter Model"
        );
        return ResponseEntity.ok(Map.of("status", "test notification sent"));
    }
}