package com.dstools.wstools.wsdatastreamproducer.config;

import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.*;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class SocketMessageHandler extends AbstractWebSocketHandler {

    private ConcurrentHashMap<WebSocketSession, Pair<Instant, Instant>> sessionHistory;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        startActiveSession(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info("Session : " + session.getId() + " Received TEXT message : " + message);
        broadcastAccrossSessions(message);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        log.info("Session : " + session.getId() + " Received BINARY message : " + message);
        broadcastAccrossSessions(message);
    }


    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) {
        log.info("Session : " + session.getId() + " Received PONG message : " + message);
        broadcastAccrossSessions(message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("Session : " + session.getId() + " Transport Error message : " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        endSession(session);
        log.debug("Session : " + session.getId() + " connection Closed with status: " + status);
    }

    @Override
    public boolean supportsPartialMessages() {
        log.debug("Partial messages not supported !!");
        return false;
    }

    private Function<ConcurrentHashMap<WebSocketSession, Pair<Instant, Instant>>, Stream<WebSocketSession>> activeSessionsFilter = sessionHistory ->
            sessionHistory.entrySet().stream()
                    .filter(sessionEntry -> sessionEntry.getValue().getValue() == null)
                    .map(Map.Entry::getKey);


    private void broadcastAccrossSessions(AbstractWebSocketMessage message) {

        List<WebSocketSession> activeSessionList = activeSessionsFilter
                .apply(sessionHistory).collect(Collectors.toList());

        log.debug("Found " + activeSessionList.size() + " sessions. Broadcasting ...");

        activeSessionList
                .forEach(activeSession -> {
                    try {
                        activeSession.sendMessage(message);
                    } catch (IOException e) {
                        log.debug("Failed to send message : " + message + " to active session " + activeSession.getId());
                    }
                });
    }

    private void startActiveSession(WebSocketSession session) {
        if (CollectionUtils.isEmpty(sessionHistory)) {
            sessionHistory = new ConcurrentHashMap<>();
        }
        Instant sessionStartInstant = Instant.now();
        sessionHistory.put(session, new Pair(sessionStartInstant, null));
        log.debug("Started session " + session.getId() + " at " + sessionStartInstant.toEpochMilli());
    }

    private void endSession(WebSocketSession session) {
        Instant sessionEndInstant = Instant.now();
        sessionHistory.entrySet().stream()
                .filter(entry -> entry.getKey().getId().equals(session.getId()))
                .findFirst()
                .ifPresent(webSocketSessionPairEntry -> {
                    WebSocketSession foundSession = webSocketSessionPairEntry.getKey();
                    Instant startInstant = webSocketSessionPairEntry.getValue().getKey();
                    Instant endInstant = webSocketSessionPairEntry.getValue().getValue();
                    long sessionDurationInNanos = Duration.between(startInstant, endInstant).toNanos();
                    log.debug("Session " + foundSession.getId() + " ended at " + sessionEndInstant.toEpochMilli() + " Duration : " + sessionDurationInNanos);
                });
    }


}
