package uk.co.developmentanddinosaurs.apps.poker.application.rooms

import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import poker.events.Event
import poker.events.PlayersEvent
import poker.models.Player
import poker.models.Vote
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class Room(val id: String) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    private val players = ConcurrentHashMap<String, Player>()
    private val playerSockets = ConcurrentHashMap<String, MutableList<WebSocketSession>>()

    suspend fun addPlayer(player: Player, socket: WebSocketSession) {
        players.computeIfAbsent(player.id) { player }
        val sockets = playerSockets.computeIfAbsent(player.id) { CopyOnWriteArrayList() }
        sockets.add(socket)
        broadcastPlayers()
    }

    suspend fun removePlayer(playerId: String, socket: WebSocketSession) {
        val sockets = playerSockets[playerId]
        sockets?.remove(socket)

        if (sockets.isNullOrEmpty()) {
            players.remove(playerId)
        }
        broadcastPlayers()
    }

    suspend fun vote(playerId: String, vote: Vote) {
        val player = players[playerId] ?: return
        player.vote = vote
        player.voted = true
        broadcastPlayers()
    }

    suspend fun revealVotes() {
        broadcastVotes()
    }

    private suspend fun broadcastPlayers() {
        val event = PlayersEvent(players.values.map { Player(it.id, it.name, Vote.HIDDEN, it.voted) })
        broadcast(event)
    }

    private suspend fun broadcastVotes() {
        val event = PlayersEvent(players.values.toList())
        broadcast(event)
    }

    private suspend fun broadcast(event: Event) {
        playerSockets.values.flatten().forEach { socket ->
            try {
                send(socket, event)
            } catch (e: Exception) {
                log.error("Failed to send event", e)
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
