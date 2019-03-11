package com.dstools.wstools.wsdatastreamproducer.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Slf4j
@Controller
public class WebsocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final String socketBroadcastUri;

    public WebsocketController(SimpMessagingTemplate simpMessagingTemplate, @Value("socket.uri.publish")String socketBroadcastUri) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.socketBroadcastUri = socketBroadcastUri;
    }

    @MessageMapping("/message")
    public void onReceivedMessage(String message){
        log.info("Received message from client :: " + message);
        this.simpMessagingTemplate.convertAndSend(socketBroadcastUri, "Publishing from server at "+ Instant.EPOCH);
    }

}
