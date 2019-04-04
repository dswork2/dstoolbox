package com.dstools.filepublisher.ws.repositories

import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent
import java.util.concurrent.ConcurrentHashMap

@Component
interface SessionRepository {

    fun addSessionEvent(subProtocolEvent: AbstractSubProtocolEvent)
    fun totalSessions(): Int
    fun allSessions(): ConcurrentHashMap<String, AbstractSubProtocolEvent>
    fun getSession(sessionId: String)
}