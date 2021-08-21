package com.jviniciusb.hellogrpc.server

interface ServerFactory<TConfiguration : Server.Configuration> {

    fun create(config: TConfiguration.() -> Unit = {}): Server
}