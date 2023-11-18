package uk.co.developmentanddinosaurs.apps.poker

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import org.w3c.dom.asList

val httpClient = HttpClient { }
fun main() {
    document.addEventListener("DOMContentLoaded", {
        setUpClickListeners()
        setUpCardClickListeners()
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
    for (card in cards) {
        card.addEventListener("click", {
            cards.forEach { it.removeClass("active") }
            card.addClass("active")
        })
    }
}

suspend fun createRoom() {
    console.log("Creating room")
    val response = httpClient.post("/rooms")
    window.location.href = response.headers["Location"] ?: ""
}
