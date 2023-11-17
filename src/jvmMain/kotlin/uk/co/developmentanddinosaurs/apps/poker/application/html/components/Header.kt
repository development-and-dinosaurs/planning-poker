package uk.co.developmentanddinosaurs.apps.poker.application.html.components

import kotlinx.html.*

fun FlowContent.header(page: String) = run {
    header {
        nav {
            div(classes = "nav-wrapper orange") {
                a(classes = "brand-logo") {
                    href = "/"
                    img {
                        height = "64"
                        src = "/poker-icon-small.png"
                    }
                }
                ul(classes = "right hide-on-med-and-down") {
                    li(classes = if (page == "how-to-play") "active" else "") {
                        a(href = "/how-to-play") { +"Learn How to Play" }
                    }
                    li {
                        a {
                            id = "create-room-nav"
                            +"Create a Room"
                        }
                    }
                    li(classes = if (page == "about") "active" else "") {
                        a(href = "/about") { +"About" }
                    }
                }
            }
        }
    }
}
