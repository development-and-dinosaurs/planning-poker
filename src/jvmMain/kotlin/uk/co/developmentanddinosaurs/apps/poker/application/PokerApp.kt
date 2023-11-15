package uk.co.developmentanddinosaurs.apps.poker.application

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import uk.co.developmentanddinosaurs.apps.poker.application.extensions.respondCss
import uk.co.developmentanddinosaurs.apps.poker.application.html.css.style
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.home
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.howToPlay

/**
 * Entry point for the Poker application.
 */
fun main() {
    val environment = applicationEngineEnvironment {
        connector {
            port = 8080
        }
        module(Application::routing)
    }
    embeddedServer(Netty, environment).start(wait = true)
}

fun Application.routing() {
    routing {
        get("/style.css") {
            call.respondCss { style() }
        }
        get("/") {
            call.respondHtml { home() }
        }
        get("/how-to-play") {
            call.respondHtml { howToPlay() }
        }
        staticResources("/", "web")
    }
}

