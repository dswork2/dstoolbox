package com.dstools.wstools.wsdatastreamproducer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    private final String socketUri;
    private final SocketMessageHandler messageHandler;
    private final String applicationDestinationPrefix;
    private final String brokerDestination;
    private final Boolean enableSendToBroker;

    public WebsocketConfig(@Value("socket.uri") String socketUri,
                           SocketMessageHandler messageHandler,
                           @Value("socket.uri.applicationDestinationPrefix") String applicationDestinationPrefix,
                           @Value("socket.uri.brokerDestination") String brokerDestination,
                           @Value("socket.uri.sendToBroker.enabled") String sendToBroker) {
        this.socketUri = socketUri;
        this.messageHandler = messageHandler;
        this.applicationDestinationPrefix = applicationDestinationPrefix;
        this.brokerDestination = brokerDestination;
        this.enableSendToBroker = Boolean.getBoolean(sendToBroker);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint(socketUri)
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("*")
                .withSockJS();

        log.debug("Registered URIs : " + registry.toString());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(applicationDestinationPrefix);
        if (enableSendToBroker) {
            registry.enableSimpleBroker(brokerDestination);
        }
    }

        @Bean
        public ServletServerContainerFactoryBean createWebSocketContainer () {
            ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
            container.setMaxTextMessageBufferSize(8192);
            container.setMaxBinaryMessageBufferSize(8192);
            return container;
        }
    }
