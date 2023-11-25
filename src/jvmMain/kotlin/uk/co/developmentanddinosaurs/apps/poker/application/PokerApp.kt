package uk.co.developmentanddinosaurs.apps.poker.application

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json
import poker.events.Event
import poker.models.Player
import poker.models.Vote
import uk.co.developmentanddinosaurs.apps.poker.application.extensions.respondCss
import uk.co.developmentanddinosaurs.apps.poker.application.html.css.style
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.home
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.howToPlay
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.room
import uk.co.developmentanddinosaurs.apps.poker.application.rooms.Room
import uk.co.developmentanddinosaurs.apps.poker.application.rooms.RoomRepository
import uk.co.developmentanddinosaurs.apps.poker.application.security.SslKeystore
import uk.co.developmentanddinosaurs.apps.poker.application.services.NameGenerator
import uk.co.developmentanddinosaurs.apps.poker.application.sessions.PokerSession
import kotlin.text.toCharArray
import kotlin.time.Duration

/**
 * Entry point for the Poker application.
 */
fun main() {
    val sslKeystore = SslKeystore()
    val environment = applicationEngineEnvironment {
        connector { }
        sslConnector(
            keyStore = sslKeystore.keyStore,
            keyAlias = sslKeystore.alias,
            keyStorePassword = { sslKeystore.password.toCharArray() },
            privateKeyPassword = { sslKeystore.password.toCharArray() }) {
            keyStorePath = sslKeystore.file
        }
        module(Application::plugins)
        module(Application::session)
        module(Application::routing)
    }
    embeddedServer(Netty, environment).start(wait = true)
}

private val roomRepository = RoomRepository(NameGenerator())

fun Application.plugins() {
    install(CallLogging)
    install(DefaultHeaders)
    install(WebSockets)
}

fun Application.session() {
    install(Sessions) {
        cookie<PokerSession>("PokerSession") {
            cookie.httpOnly = false
            cookie.maxAge = Duration.INFINITE
        }
    }
    intercept(ApplicationCallPipeline.Plugins) {
        if (call.sessions.get<PokerSession>() == null) {
            call.sessions.set(PokerSession(generateNonce(), NameGenerator().generateName()))
        }
    }
}

fun Application.routing() {
    routing {
        get("/style.css") {
            call.respondCss { style() }
        }
        get("/") {
            val session = call.sessions.get<PokerSession>() ?: throw RuntimeException("No session")
            call.respondHtml { home(session.name) }
        }
        get("/how-to-play") {
            call.respondHtml { howToPlay() }
        }
        post("/rooms") {
            val room = roomRepository.createRoom()
            call.response.header("Location", "/rooms/${room.id}")
            call.respond(HttpStatusCode.Created)
        }
        get("/rooms/{room-id}") {
            val roomId = call.parameters["room-id"] ?: throw RuntimeException("Room ID cannot be null")
            val room = roomRepository.getRoom(roomId)
            call.respondHtml { room(room.id.replace(Regex("(.)([A-Z])"), "$1 $2")) }
        }
        webSocket("/rooms/{room-id}/ws") {
            val roomId = call.parameters["room-id"] ?: throw RuntimeException("Room ID cannot be null")
            val room = roomRepository.getRoom(roomId)
            val session = call.sessions.get<PokerSession>() ?: throw RuntimeException("No session")
            val player = Player(session.id, session.name)
            room.addPlayer(player, this)
            try {
                incoming.consumeEach { frame ->
                    if(frame is Frame.Text) {
                        handleEvent(room, player.id, frame.readText())
                    }
                }
            } finally {
                room.removePlayer(player.id, this)
                if(room.isEmpty()) {
                    roomRepository.removeRoom(room.id)
                }
            }

        }
        staticResources("/", "web")
    }
}

private suspend fun handleEvent(pokerRoom: Room, playerId: String, command: String) {
    if (!command.startsWith("{")) return
    val event = Json.decodeFromString<Event>(command)
    when (event.type) {
        "vote" -> {
            pokerRoom.vote(playerId, Json.decodeFromString<Vote>(event.contents))
        }
        "revealVotes" -> {
            pokerRoom.revealVotes()
        }
        "clearVotes" -> {
            pokerRoom.clearVotes()
        }
        else -> {
            println("Client sent invalid event [${event.type}]")
        }
    }
}
