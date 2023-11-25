package uk.co.developmentanddinosaurs.apps.poker.application.html.pages

import kotlinx.html.*
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.head
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.header
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.linkButton

fun HTML.serverError() = run {
    head(title = "Server Error | Prehistoric Planning Poker")
    body {
        header(page = "home")
        div(classes = "frame") {
            div(classes = "container") {
                div(classes = "header") {
                    h1(classes = "center-align") { +"Server Error" }
                }
                div(classes = "content") {
                    div(classes = "center-align") {
                        img {
                            alt = "Room could not be found"
                            src = "/images/cards/meteor.png"
                            width = "256px"
                        }
                    }
                    p(classes = "center-align") {
                        +"Oh no, this isn't good. Look, something weird has happened and there's an error and we're not really sure what's going on."
                    }
                    p(classes = "center-align") {
                        +"Hopefully you've found this page by doing something funky and you're not just trying to live your life and have a good time with the site."
                    }
                    p(classes = "center-align") {
                        +"Listen this has probably triggered an alert somewhere so if it wasn't supposed to happen it'll probably get fixed at some point."
                    }
                    p(classes = "center-align") {
                        +"In the meantime you should probably just head home though and try to do something else."
                    }
                    div(classes = "row center-align") {
                        linkButton("Take me home", "/")
                    }
                }
            }
        }
    }
}
