package com.github.slenkis

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.util.KtorExperimentalAPI
import java.io.File

@KtorExperimentalAPI
fun Route.routes() {

    val time = TimeIO(application.getConfigProperty("time.path"))

    get("/") {
        call.respond(
            FreeMarkerContent(
                "home.ftl",
                mapOf("model" to Model(time.get()))
            )
        )
    }

    route("/time") {
        get {
            val minutes = time.get()
            call.respond(Model(minutes))
        }

        authenticate("basic") {
            // add
            put {
                try {
                    val request = call.receive<Model>()
                    if (request.minutes < 1) call.respond(HttpStatusCode.NotAcceptable)
                    else {
                        time.add(request.minutes)
                        call.respond(HttpStatusCode.OK)
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.UnsupportedMediaType)
                }
            }

            // reset
            post {
                time.reset()
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    static {
        resource("favicon.ico")
        static("static") {
            resources("js")
            resources("css")
        }
    }
}

class TimeIO(path: String) {
    private val file = File(path)

    fun get(): Int = read()
    fun add(minutes: Int): Unit = write(read() + minutes)
    fun reset(): Unit = write(0)

    private fun write(value: Int) {
        if (value >= 0)
            file.writeText(value.toString())
    }

    private fun read() =
        if (file.createNewFile()) {
            write(0)
            0
        } else file.readText().toInt()
}