package com.dstools.wsdatarelayserver.endpoints.config.publishers

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class PublishingSocketConfiguration :  WebSocketMessageBrokerConfigurer{

    private val OUTBOUND_BROKER_DESTINATION = "/outbound.broker"
    private val OUTBOUND_APP_DESTINATION = "/outbound.app"

    private val OUTBOUND_STOMP_ENDPOINT = "/outbound.ws"

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint(OUTBOUND_STOMP_ENDPOINT)
                .setAllowedOrigins("*")
                .withSockJS()
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker(OUTBOUND_BROKER_DESTINATION)
        registry.setApplicationDestinationPrefixes(OUTBOUND_APP_DESTINATION)
    }

}