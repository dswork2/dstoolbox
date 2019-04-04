package com.dstools.filepublisher.ws.repositories

import org.springframework.web.socket.messaging.AbstractSubProtocolEvent
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionConnectedEvent
import java.security.Principal
import java.util.concurrent.ConcurrentHashMap

class ConcurrentMapSessionRepository: SessionRepository{

    private lateinit var allSessionEvents : ConcurrentHashMap<Principal, AbstractSubProtocolEvent>

    override fun addSession(sessionConnectEvent: SessionConnectEvent) {
        checkNullOrEmpty()
        allSessionEvents.put(sessionConnectEvent.user!!, sessionConnectEvent)
    }


    override fun addSession(sessionConnectedEvent: SessionConnectedEvent) {
        checkNullOrEmpty()
        allSessionEvents.put(sessionConnectedEvent.user!!, sessionConnectedEvent)
    }
    private fun checkNullOrEmpty() {
        if (allSessionEvents.isNullOrEmpty()) {
            allSessionEvents = ConcurrentHashMap<Principal, AbstractSubProtocolEvent>()
        }
    }

}