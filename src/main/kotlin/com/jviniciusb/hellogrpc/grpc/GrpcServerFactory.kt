package com.jviniciusb.hellogrpc.grpc

import com.jviniciusb.hellogrpc.server.Server
import com.jviniciusb.hellogrpc.server.ServerFactory

object GrpcServerFactory : ServerFactory<GrpcServer.Configuration> {

    override fun create(config: GrpcServer.Configuration.() -> Unit): Server {
        return GrpcServer(config)
    }
}