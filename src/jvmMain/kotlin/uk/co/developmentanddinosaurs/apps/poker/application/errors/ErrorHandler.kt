package uk.co.developmentanddinosaurs.apps.poker.application.errors

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.html.respondHtml
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.pageNotFound
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.roomNotFound
import uk.co.developmentanddinosaurs.apps.poker.application.html.pages.serverError
import uk.co.developmentanddinosaurs.apps.poker.application.rooms.RoomDoesNotExistException

class ErrorHandler {
    suspend fun handle(
        call: ApplicationCall,
        cause: Throwable,
    ) {
        when (cause) {
            is RoomDoesNotExistException -> call.respondHtml(HttpStatusCode.NotFound) { roomNotFound(cause.room) }
            else -> call.respondHtml(HttpStatusCode.InternalServerError) { serverError() }
        }
    }

    suspend fun handle(
        call: ApplicationCall,
        status: HttpStatusCode,
    ) {
        when (status) {
            HttpStatusCode.NotFound -> call.respondHtml(HttpStatusCode.NotFound) { pageNotFound() }
            else -> call.respondHtml(HttpStatusCode.InternalServerError) { serverError() }
        }
    }
}
