package uk.co.developmentanddinosaurs.apps.poker.application.events

import io.kotest.core.spec.style.StringSpec
import io.mockk.coVerify
import io.mockk.mockk
import poker.events.ClearVotesEvent
import poker.events.RevealVotesEvent
import poker.events.VoteEvent
import poker.models.Player
import poker.models.Vote
import uk.co.developmentanddinosaurs.apps.poker.application.rooms.Room

class EventHandlerTest : StringSpec({

    val room = mockk<Room>(relaxed = true)
    val eventHandler = EventHandler(room)

    "handles vote event" {
        val player = Player("id", "name")
        val event = VoteEvent(Vote.ONE)

        eventHandler.handle(player, event)

        coVerify { room.vote(player, Vote.ONE) }
    }

    "handles reveal votes event" {
        val player = Player("id", "name")
        val event = RevealVotesEvent()

        eventHandler.handle(player, event)

        coVerify { room.revealVotes() }
    }

    "handles clear votes event" {
        val player = Player("id", "name")
        val event = ClearVotesEvent()

        eventHandler.handle(player, event)

        coVerify { room.clearVotes() }
    }
})
