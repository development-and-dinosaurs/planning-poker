package uk.co.developmentanddinosaurs.apps.poker.application.routing

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.websocket.webSocket
import uk.co.developmentanddinosaurs.apps.poker.application.extensions.respondCss
import uk.co.developmentanddinosaurs.apps.poker.application.html.css.style
import uk.co.developmentanddinosaurs.apps.poker.application.rooms.RoomRepository
import uk.co.developmentanddinosaurs.apps.poker.application.services.NameGenerator

private val controller = PokerAppController(RoomRepository(NameGenerator()))

/**
 * Apply routing logic to the web server.
 */
fun Application.routing() {
    routing {
        get("/style.css") {
            call.respondCss { style() }
        }
        get("/") {
            controller.home(this)
        }
        get("/how-to-play") {
            controller.howToPlay(this)
        }
        post("/rooms") {
            controller.createRoom(this)
        }
        get("/rooms/{room-id}") {
            controller.getRoom(this)
        }
        webSocket("/rooms/{room-id}/ws") {
            controller.connectToRoom(this)
        }
        staticResources("/", "web")
    }
}
