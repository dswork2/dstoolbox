package com.dstools.wsdatarelayserver.endpoints.config.ingestors

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurationSupport

@Configuration
@EnableWebSocketMessageBroker
class IngestingEndpointConfig : WebSocketMessageBrokerConfigurationSupport() {

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ingesting.broker")
                .withSockJS()
    }
}