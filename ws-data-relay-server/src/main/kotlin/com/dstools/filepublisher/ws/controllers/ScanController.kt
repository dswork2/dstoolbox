package com.dstools.filepublisher.ws.controllers

import mu.KLogging
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.stereotype.Controller
import java.security.Principal

@Controller
class ScanController{

    companion object : KLogging()

    @MessageMapping("/scan.start")
    @SendTo("/publish/scan.result")
    fun startScan(@Payload scanData: NewScanData): String {
        logger.info { "Publishing results for path: ${scanData.scanPath} includeSubDirectories: ${scanData.includeSubdirectories}" }
        return "Publishing results for path: ${scanData.scanPath} includeSubDirectories: ${scanData.includeSubdirectories}"
    }
}

data class NewScanData(val scanPath: String, val includeSubdirectories: Boolean)