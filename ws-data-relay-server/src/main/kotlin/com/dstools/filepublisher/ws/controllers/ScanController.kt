package com.dstools.filepublisher.ws.controllers

import mu.KLogging
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.stereotype.Controller
import java.security.Principal

@Controller
class ScanController{

    companion object : KLogging()

    @MessageMapping("/scan.start")
    fun startScan(@Payload scanData: NewScanData){
        logger.info { "Starting scan at path : ${scanData.scanPath} " }
    }


}

data class NewScanData(val scanPath: String, val includeSubdirectories: Boolean)