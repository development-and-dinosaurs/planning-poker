package uk.co.developmentanddinosaurs.apps.poker

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.serialization.json.Json
import org.w3c.dom.HTMLElement
import org.w3c.dom.asList
import poker.events.Event
import poker.models.Player

val httpClient = HttpClient { install(WebSockets) }
val webSocketClient = WebSocketClient(httpClient)

fun main() {
    document.addEventListener("DOMContentLoaded", {
        setUpClickListeners()
        setUpCardClickListeners()
        MainScope().launch {
            initialiseWebSocketConnection()
        }
    })
}

fun setUpClickListeners() {
    document.getElementById("create-room")?.addEventListener("click", {
        MainScope().launch { createRoom() }
    })
    document.getElementById("create-room-nav")?.addEventListener("click", {
        MainScope().launch { createRoom() }
    })
}

fun setUpCardClickListeners() {
    val cards = document.getElementsByClassName("card").asList()
    cards.forEach { card ->
        card.addEventListener("click", {
            cards.forEach { it.removeClass("active") }
            card.addClass("active")
        })
    }
}

suspend fun createRoom() {
    val response = httpClient.post("/rooms")
    window.location.href = response.headers["Location"] ?: ""
}

suspend fun initialiseWebSocketConnection() {
    try {
        if (window.location.pathname.contains("rooms/")) {
            webSocketClient.connect()
            webSocketClient.receive(::handleEvent)
        }
    } catch (e: Exception) {
        println(e.message)
    }
}

fun handleEvent(event: String) {
    val eventJson = try {
        Json.decodeFromString<Event>(event)
    } catch (e: Exception) {
        println(e.message)
        throw e
    }
    when (eventJson.type) {
        "players" -> {
            writePlayers(Json.decodeFromString(eventJson.contents))
        }
    }
}

fun writePlayers(players: List<Player>) {
    val playersSection = document.getElementById("players") as HTMLElement
    removePlayersFromSection(playersSection)
    players.map { player ->
        document.createElement("tr").apply {
                appendChild(document.createElement("td").apply {
                    textContent = player.name
                })
                appendChild(document.createElement("td").apply {
                    textContent = "?"
                })
            }
    }.forEach {
        playersSection.appendChild(it)
    }
}

fun removePlayersFromSection(playersSection: HTMLElement) {
    while (playersSection.firstChild != null) {
        playersSection.removeChild(playersSection.lastChild!!)
    }
}
