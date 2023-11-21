package poker.models

import kotlinx.serialization.Serializable

@Serializable
enum class Vote(val value: String) {
    HIDDEN("?"), ONE("1"), TWO("2"), THREE("3"), FIVE("5"), EIGHT("8"), THIRTEEN("13");

    companion object {
        fun fromString(value: String): Vote {
            return entries.first { it.value == value }
        }
    }
}
