package uk.co.developmentanddinosaurs.apps.poker.application.html.css

import kotlinx.css.*


fun CssBuilder.style() = run {
    rule(".frame") {
        border = Border(1.rem, BorderStyle.solid, Color("#ff9800"))
    }
    rule(".dino-card-title") {
        position = Position.absolute
        top = 0.rem
        left = 0.rem
        color = Color.black
        fontWeight = FontWeight.bold
        fontSize = 2.rem
        marginLeft = 5.px
    }
    rule(".active") {
        filter = "drop-shadow(0px 0px 5px #ff9800)"
    }
    h1 {
        fontFamily = "Cormorant"
    }
    h2 {
        fontFamily = "Cormorant"
        fontSize = 3.rem
        marginTop = 0.px
    }
    strong {
        fontWeight = FontWeight.bold
    }
    rule("ul.standard-list") {
        marginLeft = 2.rem
        li {
            listStyleType = ListStyleType.disc
        }
    }
    rule(".voted") {
        backgroundColor = Color("#ff9800").withAlpha(0.5)
    }
}
