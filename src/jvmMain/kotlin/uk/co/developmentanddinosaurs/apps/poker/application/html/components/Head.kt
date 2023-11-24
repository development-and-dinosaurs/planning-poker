package uk.co.developmentanddinosaurs.apps.poker.application.html.components

import kotlinx.html.*

fun HTML.head(title: String) = run {
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
        script {
            type = "text/javascript"
            src = "/planning-poker.js"
        }
        script {
            type = "text/javascript"
            src = "https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"
        }
        title(title)
        meta {
            name = "viewport"
            content = "width=device-width, initial-scale=1.0"
        }
    }
}
