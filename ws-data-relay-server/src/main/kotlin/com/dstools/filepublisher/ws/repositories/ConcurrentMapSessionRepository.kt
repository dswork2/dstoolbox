package com.dstools.filepublisher.ws.repositories

import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent
import java.util.concurrent.ConcurrentHashMap

@Component
class ConcurrentMapSessionRepository : SessionRepository {

    enum class EVENT_TYPE(eventType: String) {
        CONNECT("CONNECT"),
        CONNECTED("CONNECTED"),
        SUBSCRIBE("SUBSCRIBE"),
        UNSUBSCRIBE("UNSUBSCRIBE"),
        DISCONNECTED("DISCONNECTED")
    }

    private var allSessionEvents: ConcurrentHashMap<String, AbstractSubProtocolEvent> = ConcurrentHashMap()

    override fun totalSessions(): Int {
        return allSessionEvents.size
    }

    override fun allSessions(): ConcurrentHashMap<String, AbstractSubProtocolEvent> {
        return allSessionEvents
    }

    override fun getSession(sessionId: String) {
        allSessionEvents[sessionId]
    }

    override fun addSessionEvent(sessionConnectEvent: AbstractSubProtocolEvent) {
        allSessionEvents[extractSessionId(sessionConnectEvent)] = sessionConnectEvent
    }

    fun findCurrentSubscribers(): Map<String, AbstractSubProtocolEvent> {
        return findByEventType(EVENT_TYPE.SUBSCRIBE)
    }

    fun findUnsubscribers(): Map<String, AbstractSubProtocolEvent> {
        return findByEventType(EVENT_TYPE.UNSUBSCRIBE)
    }

    fun findDisconnected(): Map<String, AbstractSubProtocolEvent> {
        return findByEventType(EVENT_TYPE.DISCONNECTED)
    }

    fun findByEventType(eventType:EVENT_TYPE): Map<String, AbstractSubProtocolEvent> {
        return allSessionEvents.filterKeys { k -> k.equals("simpSessionType") }
                .filterValues { v->v.toString().equals(eventType.toString()) }
    }

    private fun extractSessionId(stompEvent: AbstractSubProtocolEvent) =
            stompEvent.message.headers
                    .filterKeys { k -> k.equals("simpSessionId") }
                    .get("simpSessionId")
                    .toString()
}