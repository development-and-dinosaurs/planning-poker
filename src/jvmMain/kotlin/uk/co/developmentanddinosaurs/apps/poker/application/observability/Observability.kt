package uk.co.developmentanddinosaurs.apps.poker.application.observability

import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.metrics.LongCounter
import io.opentelemetry.api.trace.Span
import org.slf4j.LoggerFactory

object Observability {
    private val log = LoggerFactory.getLogger(this.javaClass)

    fun roomCreated(room: String) {
        span("roomCreated").setAttribute("room", room).end()
        counter("roomsCreated").add(1)
    }

    fun roomJoined(
        room: String,
        player: String,
    ) {
        span("roomJoined").setAttribute("room", room).setAttribute("player", player).end()
        counter("roomsJoined").add(1)
        log.info("Player [$player] joined room [$room]")
    }

    fun roomLeft(
        room: String,
        player: String,
    ) {
        span("roomLeft").setAttribute("room", room).setAttribute("player", player).end()
        log.info("Player [$player] left room [$room]")
    }

    fun playerVoted(
        player: String,
        vote: String,
    ) {
        span("playerVoted").setAttribute("player", player).setAttribute("vote", vote).end()
        log.info("Player [$player] voted [$vote]")
    }

    fun votesRevealed(player: String) {
        span("votesRevealed").setAttribute("player", player).end()
        log.info("Player [$player] revealed the votes")
    }

    fun votesCleared(player: String) {
        span("votesCleared").setAttribute("player", player).end()
        log.info("Player [$player] cleared the votes")
    }

    fun catModeActivated(player: String) {
        span("catModeActivated").setAttribute("player", player).end()
        log.info("Player [$player] entered cat mode")
    }

    fun roomDeletionScheduled(room: String) {
        span("roomDeletionScheduled").setAttribute("room", room).end()
        log.info("Room [$room] scheduled for deletion")
    }

    fun roomDeletionCancelled(room: String) {
        span("roomDeletionCancelled").setAttribute("room", room).end()
        log.info("Room [$room] deletion cancelled")
    }

    fun roomDeleted(room: String) {
        span("roomDeleted").setAttribute("room", room).end()
        log.info("Room [$room] deleted")
    }

    fun roomDoesNotExist(room: String) {
        span("roomDoesNotExist").setAttribute("room", room).end()
        counter("roomDoesNotExist").add(1)
        log.error("Someone tried to access room [$room] which does not exist")
    }

    fun pageDoesNotExist(page: String) {
        span("pageDoesNotExist").setAttribute("page", page).end()
        counter("pageDoesNotExist").add(1)
        log.error("Someone tried to access page [$page] which does not exist")
    }

    fun error(
        status: String,
        cause: Throwable,
    ) {
        span("error").setAttribute("status", status).end()
        counter("errors").add(1)
        log.error("Something bad happened", cause)
    }

    private fun span(name: String): Span {
        return GlobalOpenTelemetry.getTracer("planning-poker").spanBuilder(name).startSpan()
    }

    private fun counter(name: String): LongCounter {
        return GlobalOpenTelemetry.getMeter("planning-poker").counterBuilder(name).build()
    }
}
