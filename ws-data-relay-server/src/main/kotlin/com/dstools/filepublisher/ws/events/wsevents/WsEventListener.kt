package com.dstools.filepublisher.ws.events.wsevents

import com.dstools.filepublisher.ws.repositories.SessionRepository
import mu.KLogging
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionConnectedEvent

/**
 * https://docs.spring.io/spring/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/websocket.html#websocket-stomp-appplication-context-events
 */

@Component
class WsEventListener(){

    lateinit var sessionRepository: SessionRepository

    companion object : KLogging()

    @EventListener
    fun handleBrokerConnection(brokerAvailabilityEvent: BrokerAvailabilityEvent){
        logger.debug { "Broker Availability Event : ${brokerAvailabilityEvent.isBrokerAvailable }"}
    }

    @EventListener
    fun handleSessionConnectEvent(sessionConnectEvent: SessionConnectEvent){
        logger.debug { "Session Connected Event : ${sessionConnectEvent.message }"}
        sessionRepository.addSession(sessionConnectEvent)
    }

    @EventListener
    fun handleSessionConnectEvent(sessionConnectedEvent: SessionConnectedEvent){
        logger.debug { "Session Connected Event : ${sessionConnectedEvent.message }"}
        sessionRepository.addSession(sessionConnectedEvent);
    }

}