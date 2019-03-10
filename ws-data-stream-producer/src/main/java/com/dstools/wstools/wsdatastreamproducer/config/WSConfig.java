package com.dstools.wstools.wsdatastreamproducer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WSConfig implements WebSocketConfigurer {

    private final String SOCKET_URI;
    private final Integer BINARY_MESSAGE_BUFFER_SIZE;

    public WSConfig(
            @Value("socket.uri") String socket_uri,
            @Value("socket.binary.message.size") Integer binary_message_buffer_size) {
        SOCKET_URI = socket_uri;
        BINARY_MESSAGE_BUFFER_SIZE = binary_message_buffer_size;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(wsHandler(), SOCKET_URI)
                .setAllowedOrigins("*");
    }

    @Bean
    public WSHandler wsHandler() {
        return new WSHandler();
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxBinaryMessageBufferSize(BINARY_MESSAGE_BUFFER_SIZE);
        return container;
    }
}
