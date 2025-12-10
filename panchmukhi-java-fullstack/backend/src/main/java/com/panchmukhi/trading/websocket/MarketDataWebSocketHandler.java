package com.panchmukhi.trading.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.panchmukhi.trading.model.MarketData;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MarketDataWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public MarketDataWebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        sessions.put(sessionId, session);
        System.out.println("WebSocket connection established: " + sessionId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = session.getId();
        sessions.remove(sessionId);
        System.out.println("WebSocket connection closed: " + sessionId + " - " + status);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received WebSocket message: " + payload);

        // Handle subscription messages
        if (payload.contains("subscribe")) {
            // Could implement subscription logic here
            sendToSession(session, "{\"type\":\"subscribed\",\"message\":\"Market data subscription active\"}");
        }
    }

    public void broadcastMarketUpdate(MarketData marketData) {
        try {
            String message = objectMapper.writeValueAsString(marketData);
            String wrappedMessage = String.format("{\"type\":\"market_update\",\"data\":%s}", message);

            sessions.values().forEach(session -> {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(new TextMessage(wrappedMessage));
                    } catch (IOException e) {
                        System.err.println("Failed to send market update to session: " + session.getId());
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Failed to broadcast market update: " + e.getMessage());
        }
    }

    public void sendToSession(WebSocketSession session, String message) {
        try {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        } catch (IOException e) {
            System.err.println("Failed to send message to session: " + session.getId());
        }
    }

    public void broadcastNewsUpdate(String newsData) {
        try {
            String wrappedMessage = String.format("{\"type\":\"news_update\",\"data\":%s}", newsData);

            sessions.values().forEach(session -> {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(new TextMessage(wrappedMessage));
                    } catch (IOException e) {
                        System.err.println("Failed to send news update to session: " + session.getId());
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Failed to broadcast news update: " + e.getMessage());
        }
    }

    public void broadcastAlert(String alertData) {
        try {
            String wrappedMessage = String.format("{\"type\":\"alert_update\",\"data\":%s}", alertData);

            sessions.values().forEach(session -> {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(new TextMessage(wrappedMessage));
                    } catch (IOException e) {
                        System.err.println("Failed to send alert to session: " + session.getId());
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Failed to broadcast alert: " + e.getMessage());
        }
    }
}