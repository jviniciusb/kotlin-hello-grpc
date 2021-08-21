package com.jviniciusb.hellogrpc.grpc

import com.jviniciusb.hellogrpc.server.Server
import io.grpc.BindableService
import io.grpc.netty.NettyServerBuilder
import java.io.File
import java.util.concurrent.TimeUnit

class GrpcServer(config: Configuration.() -> Unit) : Server {

    private val configuration = Configuration().apply(config)

    private val grpcServer = NettyServerBuilder
        .forPort(configuration.port)
        .applyConfig()
        .build()

    private fun NettyServerBuilder.applyConfig() = apply {
        if (configuration.useTransportSecurity) {
            useTransportSecurity(File(CERT_PATH), File(KEY_PATH))
        }
        configuration.services.forEach {
            addService(it)
        }
    }

    override fun start() {
        grpcServer.start()
        println("gRPC server started, listening on ${configuration.port}")
        grpcServer.awaitTermination()
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down gRPC server since JVM is shutting down")
                stop(configuration.terminationTimeoutMillis)
                println("*** server shut down")
            }
        )
    }

    override fun stop(timeoutMillis: Long) {
        grpcServer.awaitTermination(timeoutMillis, TimeUnit.MILLISECONDS)
        grpcServer.shutdown()
    }

    class Configuration : Server.Configuration() {

        var port: Int = DEFAULT_GRPC_PORT
        var useTransportSecurity = false
        var terminationTimeoutMillis = DEFAULT_TERMINATION_TIMEOUT
        var services: List<BindableService> = emptyList()
    }

    private companion object {
        const val DEFAULT_GRPC_PORT = 6565
        const val DEFAULT_TERMINATION_TIMEOUT = 1000L
        const val CERT_PATH = "cert.pem"
        const val KEY_PATH = "key.pem"
    }
}