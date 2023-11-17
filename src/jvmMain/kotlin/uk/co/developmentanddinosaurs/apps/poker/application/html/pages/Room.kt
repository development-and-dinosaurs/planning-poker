package uk.co.developmentanddinosaurs.apps.poker.application.html.pages

import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.DinosaurCard
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.card
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.head
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.header


fun HTML.room(id: String) = run {
    head(title = "$id | Prehistoric Planning Poker")
    body {
        header(page = id)
        div(classes = "frame") {
            div(classes = "container") {
                div(classes = "header") {
                    h1 { +id }
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
            }
        }
    }
}