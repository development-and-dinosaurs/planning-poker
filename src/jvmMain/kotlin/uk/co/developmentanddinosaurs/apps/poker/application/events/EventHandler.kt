package uk.co.developmentanddinosaurs.apps.poker.application.events

import kotlinx.serialization.json.Json
import poker.events.Event
import poker.models.Player
import poker.models.Vote
import uk.co.developmentanddinosaurs.apps.poker.application.rooms.Room

class EventHandler(private val room: Room) {
    suspend fun handle(
        player: Player,
        event: Event,
    ) {
        when (event.type) {
            "vote" -> {
                room.vote(player, Json.decodeFromString<Vote>(event.contents))
            }

            "revealVotes" -> {
                room.revealVotes()
            }

            "clearVotes" -> {
                room.clearVotes()
            }

            "catMode" -> {
                room.catMode(player)
            }

            else -> {
                println("Client sent invalid event [${event.type}]")
            }
        }
    }
}
