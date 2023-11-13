package uk.co.developmentanddinosaurs.apps.poker.application

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Entry point for the Poker application.
 */
fun main() {
    val environment = applicationEngineEnvironment {
        connector {
            port = 8080
        }
        module(Application::module)
    }
    embeddedServer(Netty, environment).start(wait = true)
}

fun Application.module() {
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
    }
}

