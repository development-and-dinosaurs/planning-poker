package uk.co.developmentanddinosaurs.apps.poker.application

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.sslConnector
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.server.sessions.get
import io.ktor.server.sessions.maxAge
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.util.generateNonce
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json
import poker.events.Event
import poker.models.Player
import uk.co.developmentanddinosaurs.apps.poker.application.errors.ErrorHandler
import uk.co.developmentanddinosaurs.apps.poker.application.events.EventHandler
import uk.co.developmentanddinosaurs.apps.poker.application.extensions.respondCss
import uk.co.developmentanddinosaurs.apps.poker.application.html.css.style
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.home
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.howToPlay
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.room
import uk.co.developmentanddinosaurs.apps.poker.application.rooms.RoomRepository
import uk.co.developmentanddinosaurs.apps.poker.application.security.SslKeystore
import uk.co.developmentanddinosaurs.apps.poker.application.services.NameGenerator
import uk.co.developmentanddinosaurs.apps.poker.application.sessions.PokerSession
import kotlin.time.Duration

/**
 * Entry point for the Poker application.
 */
fun main() {
    val sslKeystore = SslKeystore()
    val environment =
        applicationEngineEnvironment {
            connector { }
            sslConnector(
                keyStore = sslKeystore.keyStore,
                keyAlias = sslKeystore.alias,
                keyStorePassword = { sslKeystore.password.toCharArray() },
                privateKeyPassword = { sslKeystore.password.toCharArray() },
            ) {
                keyStorePath = sslKeystore.file
            }
            module(Application::plugins)
            module(Application::session)
            module(Application::routing)
            module(Application::errorHandling)
        }
    embeddedServer(Netty, environment).start(wait = true)
}

private fun Application.errorHandling() {
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, status ->
            ErrorHandler().handle(call, status)
        }
        exception<Throwable> { call, cause ->
            ErrorHandler().handle(call, cause)
        }
    }
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
            val eventHandler = EventHandler(room)
            val session = call.sessions.get<PokerSession>() ?: throw RuntimeException("No session")
            val player = Player(session.id, session.name)
            room.addPlayer(player, this)
            try {
                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        eventHandler.handle(player, Json.decodeFromString<Event>(frame.readText()))
                    }
                }
            } finally {
                room.removePlayer(player, this)
                if (room.isEmpty()) {
                    roomRepository.removeRoom(room.id)
                }
            }
        }
        staticResources("/", "web")
    }
}
