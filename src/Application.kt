package com.slenkis

import com.fasterxml.jackson.databind.SerializationFeature
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.jackson.jackson
import io.ktor.request.path
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.util.KtorExperimentalAPI

data class Model(val minutes: Int)

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
fun Application.module() {
    install(CORS) {
        method(HttpMethod.Get)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        allowCredentials = true
        anyHost()
    }

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(CallLogging) {
        level = org.slf4j.event.Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    routing {
        fun getProperty(name: String) = application.environment.config.property(name).getString()

        val factory = QueryFactory(
            getProperty("db.url"),
            getProperty("db.driver"),
            getProperty("db.username"),
            getProperty("db.password")
        )

        get("/") {
            val model = Model(factory.getMinutes())
            call.respond(FreeMarkerContent("home.ftl", mapOf("model" to model)))
        }

        route("/time") {
            get {
                val minutes = factory.getMinutes()
                call.respond(Model(minutes))
            }

            // add
            put {
                try {
                    val request = call.receive<Model>()
                    if (request.minutes < 1) call.respond(HttpStatusCode.NotAcceptable)
                    else {
                        factory.addMinutes(request.minutes)
                        call.respond(HttpStatusCode.OK)
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.UnsupportedMediaType)
                }

            }

            // reset
            post {
                factory.resetMinutes()
                call.respond(HttpStatusCode.OK)
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
}
