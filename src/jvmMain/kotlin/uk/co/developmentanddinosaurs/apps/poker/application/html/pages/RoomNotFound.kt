package uk.co.developmentanddinosaurs.apps.poker.application.html.pages

import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.img
import kotlinx.html.p
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.head
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.header
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.idButton

fun HTML.roomNotFound(name: String) =
    run {
        head(title = "Room Not Found | Prehistoric Planning Poker")
        body {
            header(page = "home")
            div(classes = "frame") {
                div(classes = "container") {
                    div(classes = "header") {
                        h1(classes = "center-align") { +"Room Not Found" }
                    }
                    div(classes = "content") {
                        div(classes = "center-align") {
                            img {
                                alt = "Room could not be found"
                                src = "/images/not-found.png"
                                width = "256px"
                            }
                        }
                        p(classes = "center-align") {
                            +"""We've looked everywhere but we just can't seem to find the room you're looking for. 
                            |$name, was it? Sorry, we definitely don't have a $name room.
                            """.trimMargin()
                        }
                        p(classes = "center-align") {
                            +"""Rooms are removed once all players have left, so you might be looking for an abandoned 
                            |room that has been cleaned up already.
                            """.trimMargin()
                        }
                        p(classes = "center-align") {
                            +"""We might have cleaned it up automatically by mistake too, if so, sorry about that!.
                            """.trimMargin()
                        }
                        p(classes = "center-align") {
                            +"Don't worry though, you can always create a new one!"
                        }
                        div(classes = "row center-align") {
                            idButton("Create a new room")
                        }
                    }
                }
            }
        }
    }
