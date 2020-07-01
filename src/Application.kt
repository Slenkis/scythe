package com.slenkis

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.html.respondHtml
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.*
import kotlinx.html.head
import kotlinx.html.link
import kotlinx.html.script
import kotlinx.html.title
import java.io.File
import java.math.BigDecimal

data class Model(val minutes: Int, val hours: Double)

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(CORS) {
        method(HttpMethod.Get)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        allowCredentials = true
        anyHost()
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }


    routing {
        get("/") {
            call.respondHtml {
                head {
                    title("Home")
                    script(src = "/static/home.js") {}
                    link("/static/home.css", rel = "stylesheet")
                }
            }
        }

        route("/api") {
            val file = File("resources/minutes")
            fun readFile(file: File) = file.readText().toInt()
            fun Double.roundTo1DecimalPlace() =
                BigDecimal(this).setScale(1, BigDecimal.ROUND_HALF_UP).toDouble()

            get("get") {
                val minutes = readFile(file)
                val hours = (minutes / 60.0).roundTo1DecimalPlace()
                call.respond(Model(minutes, hours))
            }

            put("add/{value}") {
                val addValue = call.parameters["value"]?.toByteOrNull() ?: 0
                if (addValue < 1) call.respond(HttpStatusCode.NotAcceptable)
                else {
                    val oldValue = readFile(file)
                    val newValue = oldValue + addValue
                    file.writeText("$newValue")
                    call.respond(HttpStatusCode.Accepted)
                }
            }

            delete("reset") {
                file.writeText("0")
                call.respond(HttpStatusCode.OK)
            }
        }

        static("static") {
            resources("js")
            resources("css")
        }
    }
}
