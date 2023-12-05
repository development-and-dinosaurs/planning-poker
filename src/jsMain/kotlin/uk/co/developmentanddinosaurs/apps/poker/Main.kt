package uk.co.developmentanddinosaurs.apps.poker

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.post
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.serialization.json.Json
import org.w3c.dom.HTMLLinkElement
import org.w3c.dom.Image
import org.w3c.dom.asList
import org.w3c.dom.events.EventListener
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.get
import poker.events.CatModeEvent
import poker.events.ClearVotesEvent
import poker.events.Event
import poker.events.RevealVotesEvent
import poker.events.VoteEvent
import poker.models.Player
import poker.models.Stats
import poker.models.Vote

val httpClient = HttpClient { install(WebSockets) }
val webSocketClient = WebSocketClient(httpClient)

fun main() {
    document.addEventListener("DOMContentLoaded", {
        setUpButtonClickListeners()
        setUpRoomClickListeners()
        setUpCardClickListeners()
        setUpCatMode()
        MainScope().launch {
            initialiseWebSocketConnection()
        }
    })
}

private fun setUpButtonClickListeners() {
    document.getElementById("create-room")?.addEventListener("click", {
        MainScope().launch { createRoom() }
    })
    document.getElementById("create-room-nav")?.addEventListener("click", {
        MainScope().launch { createRoom() }
    })
}

private fun setUpRoomClickListeners() {
    document.getElementById("reveal-votes")?.addEventListener("click", {
        MainScope().launch { revealVotes() }
    })
    document.getElementById("clear-votes")?.addEventListener("click", {
        MainScope().launch { clearVotes() }
    })
}

private fun setUpCardClickListeners() {
    val cards = document.getElementsByClassName("card").asList()
    cards.forEach { card ->
        card.addEventListener("click", {
            cards.forEach { it.removeClass("active") }
            card.addClass("active")
            val size = card.id.replace("card-", "")
            MainScope().launch { vote(size) }
        })
    }
}

private fun setUpCatMode() {
    var code = ""
    window.addEventListener(
        "keydown",
        EventListener { event ->
            val keyEvent = event as KeyboardEvent
            code += keyEvent.key.uppercase()
            if (code.substring(code.length - 7) == "CATMODE") {
                activateCatMode()
            }
        },
    )
}

private fun activateCatMode() {
    changeToCatLogo()
    changeToCatIcon()
    changeToCatTitle()
    changeToCatCards()
    MainScope().launch { sendCatModeEvent() }
}

private fun changeToCatLogo() {
    val logo = document.getElementsByClassName("logo-image")[0] as Image
    logo.src = logo.src.replace("dinosaur", "cat")
}

private fun changeToCatIcon() {
    val favicon = document.querySelector("link[rel='icon']") as HTMLLinkElement
    favicon.href = favicon.href.replace("dinosaur", "cat")
}

private fun changeToCatTitle() {
    document.title = document.title.replace("Prehistoric", "Pussycat")
}

private fun changeToCatCards() {
    val cards = document.getElementsByClassName("points-image").asList()
    cards.forEach { card ->
        card as Image
        card.src = card.src.replace("dinosaur", "cat")
    }
}

private suspend fun createRoom() {
    val response = httpClient.post("/rooms")
    window.location.href = response.headers["Location"] ?: ""
}

private suspend fun vote(size: String) {
    if (window.location.pathname.contains("rooms/")) {
        webSocketClient.sendEvent(VoteEvent(Vote.fromString(size)))
    }
}

private suspend fun revealVotes() {
    if (window.location.pathname.contains("rooms/")) {
        webSocketClient.sendEvent(RevealVotesEvent())
    }
}

private suspend fun clearVotes() {
    if (window.location.pathname.contains("rooms/")) {
        webSocketClient.sendEvent(ClearVotesEvent())
    }
}

private suspend fun sendCatModeEvent() {
    if (window.location.pathname.contains("rooms/")) {
        webSocketClient.sendEvent(CatModeEvent())
    }
}

private suspend fun initialiseWebSocketConnection() {
    try {
        if (window.location.pathname.contains("rooms/")) {
            webSocketClient.connect()
            webSocketClient.receive(::handleEvent)
        }
    } catch (e: Exception) {
        println(e.message)
    }
}

private fun handleEvent(event: String) {
    val eventJson =
        try {
            Json.decodeFromString<Event>(event)
        } catch (e: Exception) {
            println(e.message)
            throw e
        }
    when (eventJson.type) {
        "players" -> {
            writePlayers(Json.decodeFromString(eventJson.contents))
        }

        "stats" -> {
            writeStats(Json.decodeFromString(eventJson.contents))
        }

        "reset" -> {
            resetCards()
            clearStats()
        }
    }
}

private fun writePlayers(players: List<Player>) {
    val playersSection = PlayersSection(document)
    playersSection.reset()
    playersSection.write(players)
}

fun clearStats(): StatsSection {
    val statsSection = StatsSection(document)
    statsSection.reset()
    return statsSection
}

fun writeStats(stats: Stats) {
    val statsSection = clearStats()
    statsSection.write(stats)
}

private fun resetCards() {
    val cards = document.getElementsByClassName("card").asList()
    cards.forEach { it.removeClass("active") }
}
