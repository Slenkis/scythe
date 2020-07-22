package com.github.slenkis

import io.ktor.application.Application
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalAPI
fun Application.getConfigProperty(path: String) = this.environment.config.property(path).getString()

data class Model(val minutes: Int)

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
fun Application.module() {
    installAll()
    routing { routes() }
}
