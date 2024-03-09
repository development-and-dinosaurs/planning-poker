package uk.co.developmentanddinosaurs.apps.poker.application.errors

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages

val errorHandler = ErrorHandler()

/**
 * Apply error handling logic to the web server.
 */
fun Application.errorHandling() {
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, status ->
            errorHandler.handle(call, status)
        }
        exception<Throwable> { call, cause ->
            errorHandler.handle(call, cause)
        }
    }
}
