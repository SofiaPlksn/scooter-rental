package grpc.demo.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ScooterNotificationHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(ScooterNotificationHandler.class);
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("Новое WebSocket подключение: id={}, всего активных: {}",
                session.getId(), sessions.size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.debug("Сообщение от {}: {}", session.getId(), message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("WebSocket отключен: id={}, причина: {}",
                session.getId(), status.getReason());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("Ошибка транспорта для сессии {}: {}",
                session.getId(), exception.getMessage());
        sessions.remove(session);
    }

    // ИЗМЕНЕНО: Обновлен формат JSON для вашего HTML
    public void sendUserRatingNotification(Long userId, Integer score, String verdict) {
        String message = String.format(
                "{\"type\":\"USER_RATING_UPDATED\",\"userId\":%d,\"ratingScore\":%d,\"verdict\":\"%s\"}",
                userId, score, verdict
        );
        broadcast(message);
    }

    public void sendScooterCreatedNotification(Long scooterId, String model) {
        String message = String.format(
                "{\"type\":\"SCOOTER_CREATED\",\"scooterId\":%d,\"model\":\"%s\"}",
                scooterId, model
        );
        broadcast(message);
    }

    public void sendScooterDeletedNotification(Long scooterId) {
        String message = String.format(
                "{\"type\":\"SCOOTER_DELETED\",\"scooterId\":%d}",
                scooterId
        );
        broadcast(message);
    }

    // Публичный метод для broadcast (чтобы можно было вызывать из контроллера)
    public int broadcast(String message) {
        TextMessage textMessage = new TextMessage(message);
        int sent = 0;

        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(textMessage);
                    sent++;
                } catch (IOException e) {
                    log.warn("Ошибка отправки в сессию {}: {}", session.getId(), e.getMessage());
                    sessions.remove(session);
                }
            } else {
                sessions.remove(session);
            }
        }

        log.info("Broadcast: отправлено {}/{} клиентам", sent, sessions.size());
        return sent;
    }

    public int getActiveConnections() {
        return sessions.size();
    }
}