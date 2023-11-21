package poker.models

import kotlinx.serialization.Serializable

@Serializable
data class Player(val id: String, val name: String, var vote: Vote = Vote.HIDDEN)
