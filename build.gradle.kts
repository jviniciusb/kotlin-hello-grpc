import com.google.protobuf.gradle.*

val kotlinVersion: String by project
val grpcVersion: String by project
val grpcKotlinVersion: String by project
val coroutinesVersion: String by project

buildscript {
    dependencies {
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.8.17")
    }
}

plugins {
    kotlin("jvm") version "1.5.21"
    id("com.google.protobuf") version "0.8.17"
}

group = "com.jviniciusb"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // grpc
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("io.grpc:grpc-netty:$grpcVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")

    // kotlin coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    // test
    testImplementation(kotlin("test"))
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.17.3"
    }
    // generatedFilesBaseDir = "$projectDir/src/main/proto/generated"
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk7@jar"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}

sourceSets {
    main {
        java {
            srcDirs("build/generated/source/proto/main/grpc")
            srcDirs("build/generated/source/proto/main/java")
            srcDirs("build/generated/source/proto/main/grpckt")
        }
    }
}

// task to build a jar artifact
tasks.withType<Jar> {
    archiveBaseName.set("hello-grpc")
    manifest {
        attributes["Main-Class"] = "com.jviniciusb.hellogrpc.ApplicationKt"
    }
    // required to create a fat jar
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

tasks.test {
    useJUnit()
}

// required grant that stage task follows the order clean -> build
tasks.build {
    mustRunAfter("clean")
}

// stage task for heroku
tasks.register<DefaultTask>("stage") {
    dependsOn("clean", "build")
}
