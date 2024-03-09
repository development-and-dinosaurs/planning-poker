package uk.co.developmentanddinosaurs.apps.poker.application.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.util.pipeline.PipelineContext
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json
import poker.events.Event
import poker.models.Player
import uk.co.developmentanddinosaurs.apps.poker.application.events.EventHandler
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.home
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.howToPlay
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.room
import uk.co.developmentanddinosaurs.apps.poker.application.rooms.Room
import uk.co.developmentanddinosaurs.apps.poker.application.rooms.RoomRepository
import uk.co.developmentanddinosaurs.apps.poker.application.sessions.PokerSession

/**
 * Controller that ensures the correct actions are taken for HTTP routes.
 */
class PokerAppController(private val roomRepository: RoomRepository) {
    /**
     * Returns the home page.
     */
    suspend fun home(context: PipelineContext<Unit, ApplicationCall>) {
        val session = getSession(context.call)
        context.call.respondHtml { home(session.name) }
    }

    /**
     * Returns the 'how to play' page.
     */
    suspend fun howToPlay(context: PipelineContext<Unit, ApplicationCall>) {
        context.call.respondHtml { howToPlay() }
    }

    /**
     * Creates a room and returns the location.
     */
    suspend fun createRoom(context: PipelineContext<Unit, ApplicationCall>) {
        val room = roomRepository.createRoom()
        context.call.response.header("Location", "/rooms/${room.id}")
        context.call.respond(HttpStatusCode.Created)
    }

    /**
     * Returns the page for a particular room.
     */
    suspend fun getRoom(context: PipelineContext<Unit, ApplicationCall>) {
        val room = getRoom(context.call)
        context.call.respondHtml { room(room.id.replace(Regex("(.)([A-Z])"), "$1 $2")) }
    }

    /**
     * Connect to a room with a web socket connection.
     */
    suspend fun connectToRoom(context: DefaultWebSocketServerSession) {
        val room = getRoom(context.call)
        val session = getSession(context.call)
        val player = Player(session.id, session.name)
        room.addPlayer(player, context)
        listen(context, player, room)
    }

    /**
     * Listen to events from a player in a room.
     */
    private suspend fun listen(
        context: DefaultWebSocketServerSession,
        player: Player,
        room: Room,
    ) {
        val eventHandler = EventHandler(room)
        try {
            context.incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    eventHandler.handle(player, Json.decodeFromString<Event>(frame.readText()))
                }
            }
        } finally {
            room.removePlayer(player, context)
            if (room.isEmpty()) {
                roomRepository.removeRoom(room.id)
            }
        }
    }

    /**
     * Get the room referenced in this request.
     */
    private fun getRoom(call: ApplicationCall): Room {
        val roomId: String = call.parameters["room-id"] ?: throw RuntimeException("No room id")
        return roomRepository.getRoom(roomId)
    }

    /**
     * Get the session from this request.
     */
    private fun getSession(call: ApplicationCall): PokerSession {
        return call.sessions.get<PokerSession>() ?: throw RuntimeException("No session")
    }
}
