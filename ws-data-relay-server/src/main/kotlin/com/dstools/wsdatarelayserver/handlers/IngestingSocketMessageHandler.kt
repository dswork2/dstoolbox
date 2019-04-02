package com.dstools.wsdatarelayserver.handlers

import javafx.util.Pair
import mu.KLogging
import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import org.springframework.web.socket.handler.AbstractWebSocketHandler
import java.io.IOException
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

class IngestingSocketMessageHandler : AbstractWebSocketHandler() {

    companion object : KLogging()

    private lateinit var sessionHistory: ConcurrentHashMap<WebSocketSession, Pair<Instant?, Instant?>>

    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        startActiveSession(session)
    }

    @Throws(Exception::class)
    protected override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.info("Session : " + session.id + " Received TEXT message : " + message)
        broadcastAccrossSessions(message)
    }

    @Throws(Exception::class)
    protected override fun handleBinaryMessage(session: WebSocketSession, message: BinaryMessage) {
        logger.info("Session : " + session.id + " Received BINARY message : " + message)
        broadcastAccrossSessions(message)
    }

    @Throws(Exception::class)
    protected override fun handlePongMessage(session: WebSocketSession, message: PongMessage) {
        logger.info("Session : " + session.id + " Received PONG message : " + message)
        broadcastAccrossSessions(message)
    }

    @Throws(Exception::class)
    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        logger.error("Session : " + session.id + " Transport Error message : " + exception!!.message)
    }

    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        endSession(session)
        logger.debug("Session : " + session.id + " connection Closed with status: " + status)
    }

    @Throws(Exception::class)
    override fun supportsPartialMessages(): Boolean {
        logger.debug("Partial messages not supported !!")
        return false
    }

    private fun broadcastAccrossSessions(message: AbstractWebSocketMessage<*>?) {

        val activeSessionList = sessionHistory
                .filter { entry: Map.Entry<WebSocketSession, Pair<Instant?, Instant?>> -> entry.value.value == null }
                .map { it.key }

        logger.debug("Found " + activeSessionList.size + " sessions. Broadcasting ...")

        activeSessionList.forEach { activeSession ->
            try {
                activeSession.sendMessage(message!!)
            } catch (e: IOException) {
                logger.debug("Failed to send message : " + message + " to active session " + activeSession.id)
            }
        }
    }


    private fun startActiveSession(session: WebSocketSession) {
        if (sessionHistory.isNullOrEmpty()) {
            sessionHistory = ConcurrentHashMap()
        }
        val sessionStartInstant = Instant.now()
        sessionHistory.put(session, Pair(sessionStartInstant, null))
        logger.debug("Started session " + session.id + " at " + sessionStartInstant.toEpochMilli())
    }

    private fun endSession(session: WebSocketSession?) {
        val sessionEndInstant = Instant.now()
        sessionHistory.entries.stream()
                .filter { entry -> entry.key.id == session!!.id }
                .findFirst()
                .ifPresent { webSocketSessionPairEntry ->
                    val foundSession = webSocketSessionPairEntry.key
                    val startInstant = webSocketSessionPairEntry.value.key
                    val endInstant = webSocketSessionPairEntry.value.value
                    val sessionDurationInNanos = Duration.between(startInstant, endInstant).toNanos()
                    logger.debug("Session " + foundSession.id + " ended at " + sessionEndInstant.toEpochMilli() + " Duration : " + sessionDurationInNanos)
                }
    }

}


