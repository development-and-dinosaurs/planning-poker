package poker.events

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import poker.models.Player
import poker.models.Vote

@Serializable
open class Event(val type: String, val contents: String = "")

class PlayersEvent(players: List<Player>) : Event("players", Json.encodeToString(players))

class VoteEvent(vote: Vote): Event("vote", Json.encodeToString(vote))

class RevealVotesEvent: Event("revealVotes")

class ClearVotesEvent: Event("clearVotes")

class ResetEvent: Event("reset")
