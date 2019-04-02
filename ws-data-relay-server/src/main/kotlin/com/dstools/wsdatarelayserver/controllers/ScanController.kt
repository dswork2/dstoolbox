package com.dstools.wsdatarelayserver.controllers

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.stereotype.Controller

@Controller("/scan")
class ScanController{

    @SubscribeMapping("/result")
    fun scanResult() {

    }

    @MessageMapping("/start")
    fun startScan(){

    }


}