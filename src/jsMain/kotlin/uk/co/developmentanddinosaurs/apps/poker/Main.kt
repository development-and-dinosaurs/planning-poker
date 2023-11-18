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
import org.w3c.dom.asList

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
    println(event)
}
