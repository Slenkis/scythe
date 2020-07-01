package com.slenkis

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.html.respondHtml
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.*
import kotlinx.html.body
import kotlinx.html.head
import java.io.File

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        allowCredentials = true
        anyHost()
    }
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        val filePath = "resources/db"

        get("/") {
            call.respondHtml() {
                head { }
                body {
                    +"привет"
                }
            }
        }

        route("/api") {
            put("add/{value}") {
                val addValue = call.parameters["value"]?.toIntOrNull() ?: 0
                if (addValue < 1) call.respond(HttpStatusCode.NotAcceptable)
                else {
                    val oldValue = File(filePath).readText().substringAfter("= ").toInt()
                    val newValue = oldValue + addValue
                    File(filePath).writeText("minutes = $newValue")
                    call.respond(HttpStatusCode.Accepted)
                }
            }

            delete("reset") {
                File(filePath).writeText("minutes = 0")
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
