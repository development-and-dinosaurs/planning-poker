package uk.co.developmentanddinosaurs.apps.poker.application.rooms

import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import poker.events.Event
import poker.events.PlayersEvent
import poker.models.Player
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class Room(val id: String) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    private val players = ConcurrentHashMap<Player, MutableList<WebSocketSession>>()

    suspend fun addPlayer(player: Player, socket: WebSocketSession) {
        val sockets = players.computeIfAbsent(player) { CopyOnWriteArrayList() }
        sockets.add(socket)
        broadcastPlayers()
    }

    suspend fun removePlayer(player: Player, socket: WebSocketSession) {
        val sockets = players[player]

        sockets?.remove(socket)

        if (sockets.isNullOrEmpty()) {
            players.remove(player)
        }
        broadcastPlayers()
    }

    private suspend fun broadcastPlayers() {
        players.values.flatten().forEach { socket ->
            try {
                val event = PlayersEvent(players.keys.toList())
                send(socket, event)
            } catch (e: Exception) {
                log.error("Failed to send players event", e)
                try {
                    socket.close(CloseReason(CloseReason.Codes.PROTOCOL_ERROR, "Error sending message"))
                } catch (ignore: ClosedSendChannelException) {
                    // ignore
                }
            }
        }
    }

    private suspend fun send(socket: WebSocketSession, event: Event) {
       socket.send(Frame.Text(Json.encodeToString(event)))
    }
}
