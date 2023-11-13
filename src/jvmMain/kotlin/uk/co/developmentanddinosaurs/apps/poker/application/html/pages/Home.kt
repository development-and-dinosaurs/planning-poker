package uk.co.developmentanddinosaurs.apps.poker.application.html.pages

import kotlinx.html.*
import uk.co.developmentanddinosaurs.apps.poker.application.services.NameGenerator

private val nameGenerator = NameGenerator()

fun HTML.home() = run {
    head {
        link {
            rel = "stylesheet"
            href = "https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css"
        }
        link {
            rel = "stylesheet"
            href = "/style.css"
        }
        link {
            rel = "icon"
            href = "/images/cards/tyrannosaurus-rex.png"
        }
        title("Prehistoric Planning Poker")
        meta {
            name = "viewport"
            content = "width=device-width, initial-scale=1.0"
        }
    }
    body(classes = "page frame") {
        div(classes = "container") {
            div(classes = "header") {
                h1 { +"Prehistoric Planning Poker" }
            }
            div(classes = "content") {
                p { +"Welcome ${nameGenerator.generateName()}, to Prehistoric Planning Poker!" }
            }
        }
    }
}