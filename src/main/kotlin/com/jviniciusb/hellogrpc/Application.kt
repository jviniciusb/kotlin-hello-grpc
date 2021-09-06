package com.jviniciusb.hellogrpc

import com.jviniciusb.hellogrpc.grpc.GrpcServerFactory
import com.jviniciusb.hellogrpc.grpc.services.GreeterGrpcService

fun main() {
    GrpcServerFactory.create(config = {
        port = 6564
        useTransportSecurity = false
        services = listOf(GreeterGrpcService())
    }).start()
}