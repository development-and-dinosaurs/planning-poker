package poker.models

import kotlinx.serialization.Serializable

@Serializable
data class Stats(val votes: Map<Vote, Int>, val average: Average)

@Serializable
data class Average(val mode: Vote, val mean: Double)
