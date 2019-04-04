package com.dstools.filepublisher.ws.repositories

import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionConnectedEvent

@Component
interface SessionRepository {

    fun addSession(sessionConnectEvent: SessionConnectEvent)
    fun addSession(sessionConnectedEvent: SessionConnectedEvent)

}