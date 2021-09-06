package com.jviniciusb.hellogrpc.grpc.services

import com.jviniciusb.hellogrpc.proto.GreeterServiceGrpcKt
import com.jviniciusb.hellogrpc.proto.HelloReply
import com.jviniciusb.hellogrpc.proto.HelloRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform

class GreeterGrpcService : GreeterServiceGrpcKt.GreeterServiceCoroutineImplBase() {

    override suspend fun sayHello(request: HelloRequest): HelloReply {
        delay(COMPUTATION_SIMULATION_TIME)
        return HelloReply.newBuilder().setMessage("Hello ${request.name}!").build()
    }

    override suspend fun sayHelloToStream(requests: Flow<HelloRequest>): HelloReply {
        val names = mutableListOf<String>()
        requests.collect {
            names.add(it.name)
        }
        return HelloReply.newBuilder().setMessage("Hello ${names.joinToString()}!").build()
    }

    override fun sayStreamHello(request: HelloRequest): Flow<HelloReply> = flow {
        repeat(5) {
            delay(COMPUTATION_SIMULATION_TIME)
            emit(HelloReply.newBuilder().setMessage("Hello ${request.name} $it!").build())
        }
    }

    override fun sayStreamHelloToStream(requests: Flow<HelloRequest>): Flow<HelloReply> {
        return requests.transform { request ->
            repeat(2) {
                delay(COMPUTATION_SIMULATION_TIME)
                emit(HelloReply.newBuilder().setMessage("Hello ${request.name} $it!").build())
            }
        }
    }

    private companion object {
        // Value in milliseconds to be used with dealy() to simulates some computation
        const val COMPUTATION_SIMULATION_TIME = 1000L
    }
}