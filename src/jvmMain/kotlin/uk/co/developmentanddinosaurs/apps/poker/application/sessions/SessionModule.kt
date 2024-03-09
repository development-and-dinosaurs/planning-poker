package uk.co.developmentanddinosaurs.apps.poker.application.sessions

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.server.sessions.get
import io.ktor.server.sessions.maxAge
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import io.ktor.util.generateNonce
import uk.co.developmentanddinosaurs.apps.poker.application.services.NameGenerator
import kotlin.time.Duration

/**
 * Apply session management to the web server.
 */
fun Application.session() {
    install(Sessions) {
        cookie<PokerSession>("PokerSession") {
            cookie.httpOnly = false
            cookie.maxAge = Duration.INFINITE
        }
    }
    intercept(ApplicationCallPipeline.Plugins) {
        if (call.sessions.get<PokerSession>() == null) {
            call.sessions.set(PokerSession(generateNonce(), NameGenerator().generateName()))
        }
    }
}
