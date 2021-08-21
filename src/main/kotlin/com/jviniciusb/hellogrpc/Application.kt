package com.jviniciusb.hellogrpc

import com.jviniciusb.hellogrpc.grpc.GrpcServerFactory
import com.jviniciusb.hellogrpc.grpc.services.GreeterGrpcService

fun main() {
    GrpcServerFactory.create(config = {
        useTransportSecurity = true
        services = listOf(GreeterGrpcService())
    }).start()
}