package uk.co.developmentanddinosaurs.apps.poker.application.html.components

import kotlinx.html.HTML
import kotlinx.html.head
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.script
import kotlinx.html.title
import uk.co.developmentanddinosaurs.apps.poker.application.Configuration

fun HTML.head(title: String) =
    run {
        head {
            link {
                rel = "stylesheet"
                href = "https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css"
            }
            link {
                rel = "stylesheet"
                href = "/style.css?v=${Configuration.version}"
            }
            link {
                rel = "icon"
                href = "/images/cards/dinosaur/dinosaur-8.png"
            }
            script {
                type = "text/javascript"
                src = "/planning-poker.js?v=${Configuration.version}"
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
