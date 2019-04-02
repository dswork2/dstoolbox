package com.dstools.wsdatarelayserver.endpoints.config.ingestors

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class IngestingEndpointConfig : WebSocketMessageBrokerConfigurer {


    private val INBOUND_BROKER_DESTINATION = "/inbound.broker"
    private val INBOUND_APP_DESTINATION = "/inbound.app"

    private val INBOUND_STOMP_ENDPOINT = "/inbound.ws"

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint(INBOUND_STOMP_ENDPOINT)
                .setAllowedOrigins("*")
                .withSockJS()
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker(INBOUND_BROKER_DESTINATION)
        registry.setApplicationDestinationPrefixes(INBOUND_APP_DESTINATION)
    }

}