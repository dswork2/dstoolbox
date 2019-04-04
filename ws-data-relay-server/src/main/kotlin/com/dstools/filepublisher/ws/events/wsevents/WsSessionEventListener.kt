package com.dstools.filepublisher.ws.events.wsevents

import com.dstools.filepublisher.ws.repositories.SessionRepository
import mu.KLogging
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.*

/**
 * https://docs.spring.io/spring/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/websocket.html#websocket-stomp-appplication-context-events
 */

@Component
class WsEventListener(var sessionRepository: SessionRepository) {

    companion object : KLogging()

    @EventListener
    fun handleBrokerConnection(brokerAvailabilityEvent: BrokerAvailabilityEvent){
        logger.debug { "Broker Availability Event : ${brokerAvailabilityEvent.isBrokerAvailable }"}
    }

    @EventListener
    fun handleSessionConnectEvent(connectEvent: SessionConnectEvent){
        logger.debug { "Session CONNECT Event : ${connectEvent.message }"}
        sessionRepository.addSessionEvent(connectEvent)
    }

    @EventListener
    fun handleSessionConnectEvent(connectedEvent: SessionConnectedEvent){
        logger.debug { "Session CONNECTED Event : ${connectedEvent.message }"}
        sessionRepository.addSessionEvent(connectedEvent);
    }

    @EventListener
    fun handleNewSubscribeEvent(subscribeEvent: SessionSubscribeEvent){
        logger.debug { "Session SUBSCRIBEd by ${subscribeEvent.message}" }
        sessionRepository.addSessionEvent(subscribeEvent)
    }

    @EventListener
    fun handleUnSubscribeEvent(unsubscribeEvent: SessionUnsubscribeEvent){
        logger.debug { "Session UNSUBSCRIBEd by ${unsubscribeEvent.message}" }
        sessionRepository.addSessionEvent(unsubscribeEvent)
    }

    @EventListener
    fun handleUnSubscribeEvent(sessionDisconnectEvent: SessionDisconnectEvent){
        logger.debug { "Session DISCONNECTed by ${sessionDisconnectEvent.message}" }
        sessionRepository.addSessionEvent(sessionDisconnectEvent)
    }
}