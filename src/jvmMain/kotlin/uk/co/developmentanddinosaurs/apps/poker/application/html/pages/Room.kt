package uk.co.developmentanddinosaurs.apps.poker.application.html.pages

import kotlinx.html.*
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.*

fun HTML.room(roomId: String) = run {
    head(title = "$roomId | Prehistoric Planning Poker")
    body {
        header(page = roomId)
        div(classes = "frame") {
            div(classes = "container") {
                div(classes = "header") {
                    h1 { +roomId }
                }
                div(classes = "cards") {
                    div(classes = "row") {
                        card(DinosaurCard.DINOSAUR_EGG)
                        card(DinosaurCard.DILOPHOSAURUS)
                        card(DinosaurCard.VELOCIRAPTOR)
                        card(DinosaurCard.TRICERATOPS)
                        card(DinosaurCard.TYRANNOSAURUS_REX)
                        card(DinosaurCard.METEOR)
                    }
                }
                div(classes = "row") {
                    div(classes = "col s6") {
                        idButton("Reveal votes")
                    }
                    div(classes = "col s6") {
                        idButton("Clear votes")
                    }
                }
                div(classes = "row") {
                    div(classes = "col s6") {
                        h2 { +"Players" }
                    }
                    div(classes = "col s6") {
                        h2 { +"Stats" }
                    }
                    div(classes = "col s6") {
                        table {
                            thead {
                                tr {
                                    th { +"Player" }
                                    th { +"Vote" }
                                }
                            }
                            tbody {
                                id = "players"
                            }
                        }
                    }
                    div(classes = "col s6") {
                        table {
                            thead {
                                tr {
                                    th { +"Estimate" }
                                    th { +"Value" }
                                }
                            }
                            tbody {
                                id = "stats"
                            }
                        }
                    }
                }
            }
        }
    }
}