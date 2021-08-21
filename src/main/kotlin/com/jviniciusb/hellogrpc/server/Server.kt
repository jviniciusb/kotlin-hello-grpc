package com.jviniciusb.hellogrpc.server

interface Server {

    fun start()

    fun stop(timeoutMillis: Long)

    open class Configuration
}