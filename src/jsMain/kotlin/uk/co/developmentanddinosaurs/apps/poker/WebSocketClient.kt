package uk.co.developmentanddinosaurs.apps.poker

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.browser.window

class WebSocketClient(private val httpClient: HttpClient) {
    private lateinit var session: WebSocketSession

    var isConnected = false

    suspend fun connect() {
        val host = window.location.hostname
        val port = window.location.port
        val path = window.location.pathname + "/ws"
        session = httpClient.webSocketSession("ws://$host:$port$path")
        isConnected = true
    }

    suspend fun receive(onReceive: (input: String) -> Unit) {
        if(!isConnected) throw WebSocketException("Cannot receive before connection is established")
        while (true) {
            val frame = session.incoming.receive()
            if (frame is Frame.Text) {
                onReceive(frame.readText())
            }
        }
    }

}
