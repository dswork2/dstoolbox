package com.dstools.wstools.wsdatastreamproducer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class DataRecieverWebsocketConfig implements WebSocketConfigurer {
    private final DataRecieverWebsocketHandler dataRecieverWebsocketHandler;

    public DataRecieverWebsocketConfig(DataRecieverWebsocketHandler dataRecieverWebsocketHandler) {
        this.dataRecieverWebsocketHandler = dataRecieverWebsocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(dataRecieverWebsocketHandler,"/data-out");
    }

}
