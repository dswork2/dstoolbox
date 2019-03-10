package com.dstools.wstools.wsdatastreamproducer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class WSHandler extends AbstractWebSocketHandler {

    private List<WebSocketSession> sessionList = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Created new session " + session.getId());
        sessionList.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage messageReceived) throws Exception {
        log.info("New Text Message Received from session " + session.getId());
        session.sendMessage(messageReceived);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage messageReceived) throws IOException {
        log.info("New Binary Message Received from session " + session.getId());
        session.sendMessage(messageReceived);
    }
}
