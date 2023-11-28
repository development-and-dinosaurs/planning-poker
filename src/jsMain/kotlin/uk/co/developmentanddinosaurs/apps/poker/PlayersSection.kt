package uk.co.developmentanddinosaurs.apps.poker

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import poker.models.Player

class PlayersSection(private val document: Document) {

    private val playersSection = document.getElementById("players") as HTMLElement
    private val dinosaurEmoji = "\uD83E\uDD96" // 🦖

    fun reset() {
        playersSection.innerHTML = ""
    }

    fun write(players: List<Player>) {
        players.map { player -> row(player) }.forEach {
            playersSection.appendChild(it)
        }
    }

    private fun row(player: Player): Element {
        return document.createElement("tr").apply {
            className = if (player.voted) "voted" else ""
            val icon = if (document.cookie.contains(player.id)) "$dinosaurEmoji " else ""
            appendChild(column(icon + player.name))
            appendChild(column(player.vote.value))
        }
    }

    private fun column(content: String): Element {
        return document.createElement("td").apply {
            textContent = content
        }
    }
}