package uk.co.developmentanddinosaurs.apps.poker.application.html.css

import kotlinx.css.*


fun CssBuilder.style() = run {
    rule(".frame") {
        border = Border(1.rem, BorderStyle.solid, Color("#ff9800"))
    }
    h1 {
        fontFamily = "Cormorant"
    }
}
