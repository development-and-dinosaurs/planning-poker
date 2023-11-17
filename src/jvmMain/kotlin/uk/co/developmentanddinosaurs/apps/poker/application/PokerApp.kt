package uk.co.developmentanddinosaurs.apps.poker.application

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import uk.co.developmentanddinosaurs.apps.poker.application.extensions.respondCss
import uk.co.developmentanddinosaurs.apps.poker.application.html.css.style
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.home
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.howToPlay
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.room
import uk.co.developmentanddinosaurs.apps.poker.application.rooms.RoomRepository
import uk.co.developmentanddinosaurs.apps.poker.application.services.NameGenerator

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

private val roomRepository = RoomRepository(NameGenerator())

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
        post("/rooms") {
            val room = roomRepository.createRoom()
            call.response.header("Location", "/rooms/${room.id}")
            call.respond(HttpStatusCode.Created)
        }
        get("/rooms/{room}") {
            val room = roomRepository.getRoom(call.parameters["room"]!!)
            call.respondHtml { room(room.id.replace(Regex("(.)([A-Z])"), "$1 $2")) }
        }
        staticResources("/", "web")
    }
}

