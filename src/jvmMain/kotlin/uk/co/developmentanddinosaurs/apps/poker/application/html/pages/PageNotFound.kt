package uk.co.developmentanddinosaurs.apps.poker.application.html.pages

import kotlinx.html.*
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.head
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.header
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.linkButton

fun HTML.pageNotFound() = run {
    head(title = "Page Not Found | Prehistoric Planning Poker")
    body {
        header(page = "home")
        div(classes = "frame") {
            div(classes = "container") {
                div(classes = "header") {
                    h1(classes = "center-align") { +"Page Not Found" }
                }
                div(classes = "content") {
                    div(classes = "center-align") {
                        img {
                            alt = "Page could not be found"
                            src = "/images/not-found.png"
                            width = "256px"
                        }
                    }
                    p(classes = "center-align") {
                        +"We've looked everywhere but we just can't seem to find the page you're looking for."
                    }
                    p(classes = "center-align") {
                        +"Are you trying to find some hidden easter egg pages?"
                    }
                    p(classes = "center-align") {
                        +"I promise there aren't any, so you're just wasting your time."
                    }
                    p(classes = "center-align") {
                        +"It'd be pretty bad if you've clicked a link on the site and arrived here, but that really shouldn't happen, so it's probably not that."
                    }
                    p(classes = "center-align") {
                        +"Best bet is to head back to the home page, and then just follow the links to what you want from there. No more exploring!"
                    }
                    div(classes = "row center-align") {
                        linkButton("Take me home", "/")
                    }
                }
            }
        }
    }
}
