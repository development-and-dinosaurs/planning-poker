package uk.co.developmentanddinosaurs.apps.poker.application

import io.ktor.server.application.Application
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.sslConnector
import io.ktor.server.netty.Netty
import uk.co.developmentanddinosaurs.apps.poker.application.errors.errorHandling
import uk.co.developmentanddinosaurs.apps.poker.application.plugins.plugins
import uk.co.developmentanddinosaurs.apps.poker.application.routing.routing
import uk.co.developmentanddinosaurs.apps.poker.application.security.SslKeystore
import uk.co.developmentanddinosaurs.apps.poker.application.sessions.session

/**
 * Entry point for the Poker application.
 */
fun main() {
    val sslKeystore = SslKeystore()
    val environment =
        applicationEngineEnvironment {
            connector { }
            sslConnector(
                keyStore = sslKeystore.keyStore,
                keyAlias = sslKeystore.alias,
                keyStorePassword = { sslKeystore.password.toCharArray() },
                privateKeyPassword = { sslKeystore.password.toCharArray() },
            ) {
                keyStorePath = sslKeystore.file
            }
            module(Application::plugins)
            module(Application::session)
            module(Application::routing)
            module(Application::errorHandling)
        }
    embeddedServer(Netty, environment).start(wait = true)
}
