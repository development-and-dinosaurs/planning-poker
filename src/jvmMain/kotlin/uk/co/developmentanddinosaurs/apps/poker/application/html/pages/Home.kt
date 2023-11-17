package uk.co.developmentanddinosaurs.apps.poker.application.html.pages

import kotlinx.html.*
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.DinosaurCard
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.card
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.head
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.header
import uk.co.developmentanddinosaurs.apps.poker.application.services.NameGenerator

private val nameGenerator = NameGenerator()

fun HTML.home() = run {
    head(title = "Prehistoric Planning Poker")
    body {
        header(page = "home")
        div(classes = "frame") {
            div(classes = "container") {
                div(classes = "header") {
                    h1 { +"Prehistoric Planning Poker" }
                }
                div(classes = "content") {
                    p {
                        +"Welcome "
                        strong { +nameGenerator.generateName() }
                        +", to Prehistoric Planning Poker!"
                    }
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
                    p {
                        +"Prehistoric Planning Poker is the coolest way to run agile estimation sessions, allowing remote and in-person teams to come to consensus more quickly, and in style!"
                    }
                    p {
                        +"You can get started quickly by following these steps:"
                    }
                    ul(classes = "standard-list") {
                        li { +"Create a room" }
                        li { +"Invite your team" }
                        li { +"Discuss a story" }
                        li { +"Vote" }
                        li { +"Reveal" }
                        li { +"Discuss" }
                        li { +"Repeat as necessary" }
                    }
                    div(classes = "row") {
                        div(classes = "col s6 l3 offset-l3") {
                            a(classes = "waves-effect waves-light btn-large orange") {
                                href = "/how-to-play"
                                +"How to play"
                            }
                        }
                        div(classes = "col s6 l3") {
                            a(classes = "waves-effect waves-light btn-large orange") {
                                id = "create-room"
                                +"Create room"
                            }
                        }
                    }
                }
            }
        }
    }
}