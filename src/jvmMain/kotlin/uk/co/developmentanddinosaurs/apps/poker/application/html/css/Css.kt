package uk.co.developmentanddinosaurs.apps.poker.application.html.css

import kotlinx.css.Border
import kotlinx.css.BorderStyle
import kotlinx.css.Color
import kotlinx.css.CssBuilder
import kotlinx.css.FontWeight
import kotlinx.css.ListStyleType
import kotlinx.css.Position
import kotlinx.css.backgroundColor
import kotlinx.css.border
import kotlinx.css.color
import kotlinx.css.filter
import kotlinx.css.fontFamily
import kotlinx.css.fontSize
import kotlinx.css.fontWeight
import kotlinx.css.h1
import kotlinx.css.h2
import kotlinx.css.left
import kotlinx.css.li
import kotlinx.css.listStyleType
import kotlinx.css.marginLeft
import kotlinx.css.marginTop
import kotlinx.css.position
import kotlinx.css.px
import kotlinx.css.rem
import kotlinx.css.strong
import kotlinx.css.top

fun CssBuilder.style() =
    run {
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
            child(li.tagName) {
                listStyleType = ListStyleType.disc
            }
        }
        rule(".voted") {
            backgroundColor = Color("#ff9800").withAlpha(0.5)
        }
    }
