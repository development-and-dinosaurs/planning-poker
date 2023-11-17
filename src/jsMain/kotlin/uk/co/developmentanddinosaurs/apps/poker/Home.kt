package uk.co.developmentanddinosaurs.apps.poker

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

val httpClient = HttpClient { }
fun main() {
    document.addEventListener("DOMContentLoaded", {
        setUpClickListeners()
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

suspend fun createRoom() {
    console.log("Creating room")
    val response = httpClient.post("/rooms")
    window.location.href = response.headers["Location"] ?: ""
}
