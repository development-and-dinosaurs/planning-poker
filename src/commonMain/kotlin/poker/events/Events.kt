package poker.events

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import poker.models.Player

@Serializable
open class Event(val type: String, val contents: String)

class PlayersEvent(players: List<Player>) : Event("players", Json.encodeToString(players))
