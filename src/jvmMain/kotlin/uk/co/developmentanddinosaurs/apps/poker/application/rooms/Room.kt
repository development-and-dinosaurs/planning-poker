package uk.co.developmentanddinosaurs.apps.poker.application.rooms

import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import poker.events.Event
import poker.events.PlayersEvent
import poker.events.ResetEvent
import poker.events.StatsEvent
import poker.models.Average
import poker.models.Player
import poker.models.Stats
import poker.models.Vote
import uk.co.developmentanddinosaurs.apps.poker.application.observability.Observability
import uk.co.developmentanddinosaurs.apps.poker.application.statistics.Statistics
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class Room(val id: String) {
    init {
        Observability.roomCreated(id)
    }

    private val players = ConcurrentHashMap<String, Player>()
    private val playerSockets = ConcurrentHashMap<String, MutableList<WebSocketSession>>()

    suspend fun addPlayer(
        player: Player,
        socket: WebSocketSession,
    ) {
        players.computeIfAbsent(player.id) { player }
        val sockets = playerSockets.computeIfAbsent(player.id) { CopyOnWriteArrayList() }
        sockets.add(socket)
        Observability.roomJoined(id, player.name)
        broadcastPlayers()
    }

    suspend fun removePlayer(
        player: Player,
        socket: WebSocketSession,
    ) {
        val sockets = playerSockets[player.id]
        sockets?.remove(socket)

        if (sockets.isNullOrEmpty()) {
            players.remove(player.id)
        }
        Observability.roomLeft(id, player.name)
        broadcastPlayers()
    }

    suspend fun vote(
        player: Player,
        vote: Vote,
    ) {
        val roomPlayer = players[player.id] ?: return
        roomPlayer.vote = vote
        roomPlayer.voted = true
        Observability.playerVoted(player.name, vote.name)
        broadcastPlayers()
    }

    suspend fun revealVotes(player: Player) {
        broadcastVotes()
        broadcastStats()
        Observability.votesRevealed(player.name)
    }

    suspend fun clearVotes(player: Player) {
        players.values.forEach {
            it.vote = Vote.HIDDEN
            it.voted = false
        }
        Observability.votesCleared(player.name)
        broadcastPlayers()
        broadcastReset()
    }

    suspend fun catMode(player: Player) {
        val roomPlayer = players[player.id] ?: return
        roomPlayer.catMode = true
        Observability.catModeActivated(player.name)
        broadcastPlayers()
    }

    fun isEmpty(): Boolean {
        return players.isEmpty()
    }

    private suspend fun broadcastPlayers() {
        val event = PlayersEvent(players.values.map { Player(it.id, it.name, Vote.HIDDEN, it.voted, it.catMode) })
        broadcast(event)
    }

    private suspend fun broadcastVotes() {
        val event = PlayersEvent(players.values.toList())
        broadcast(event)
    }

    private suspend fun broadcastStats() {
        val votes = players.values.map { it.vote }
        val statistics = Statistics(votes)
        val event = StatsEvent(Stats(statistics.voteDistribution, Average(statistics.mode, statistics.mean)))
        broadcast(event)
    }

    private suspend fun broadcastReset() {
        val event = ResetEvent()
        broadcast(event)
    }

    private suspend fun broadcast(event: Event) {
        playerSockets.values.flatten().forEach { socket ->
            try {
                send(socket, event)
            } catch (e: Exception) {
                try {
                    socket.close(CloseReason(CloseReason.Codes.PROTOCOL_ERROR, "Error sending message"))
                } catch (ignore: ClosedSendChannelException) {
                    // ignore
                }
            }
        }
    }

    private suspend fun send(
        socket: WebSocketSession,
        event: Event,
    ) {
        socket.send(Frame.Text(Json.encodeToString(event)))
    }
}
