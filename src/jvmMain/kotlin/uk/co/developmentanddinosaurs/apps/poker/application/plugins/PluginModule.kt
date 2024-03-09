package uk.co.developmentanddinosaurs.apps.poker.application.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.websocket.WebSockets

/**
 * Apply plugins required by the web server.
 */
fun Application.plugins() {
    install(CallLogging)
    install(DefaultHeaders)
    install(WebSockets)
}
