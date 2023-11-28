package uk.co.developmentanddinosaurs.apps.poker

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSocketException
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import kotlinx.browser.window
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import poker.events.Event

class WebSocketClient(private val httpClient: HttpClient) {
    private lateinit var session: WebSocketSession

    private var isConnected = false

    suspend fun connect() {
        val host = window.location.hostname
        val port = window.location.port
        val path = window.location.pathname + "/ws"
        session = httpClient.webSocketSession("wss://$host:$port$path")
        isConnected = true
    }

    suspend fun receive(onReceive: (input: String) -> Unit) {
        if (!isConnected) throw WebSocketException("Cannot receive before connection is established")
        while (true) {
            val frame = session.incoming.receive()
            if (frame is Frame.Text) {
                onReceive(frame.readText())
            }
        }
    }

    suspend fun sendEvent(event: Event) {
        session.send(Frame.Text(Json.encodeToString(event)))
    }
}
