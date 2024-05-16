package uk.co.developmentanddinosaurs.apps.poker.application.errors

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.html.respondHtml
import io.ktor.server.request.uri
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.pageNotFound
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.roomNotFound
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.serverError
import uk.co.developmentanddinosaurs.apps.poker.application.observability.Observability
import uk.co.developmentanddinosaurs.apps.poker.application.rooms.RoomDoesNotExistException

class ErrorHandler {
    suspend fun handle(
        call: ApplicationCall,
        cause: Throwable,
    ) {
        when (cause) {
            is RoomDoesNotExistException -> {
                Observability.roomDoesNotExist(cause.room)
                call.respondHtml(HttpStatusCode.NotFound) { roomNotFound(cause.room) }
            }
            else -> {
                Observability.error("500", cause)
                call.respondHtml(HttpStatusCode.InternalServerError) { serverError() }
            }
        }
    }

    suspend fun handle(
        call: ApplicationCall,
        status: HttpStatusCode,
    ) {
        when (status) {
            HttpStatusCode.NotFound -> {
                Observability.pageDoesNotExist(call.request.uri)
                call.respondHtml(HttpStatusCode.NotFound) { pageNotFound() }
            }
            else -> call.respondHtml(HttpStatusCode.InternalServerError) { serverError() }
        }
    }
}
