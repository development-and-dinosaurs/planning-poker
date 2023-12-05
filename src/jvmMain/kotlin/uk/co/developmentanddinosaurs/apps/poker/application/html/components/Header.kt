package uk.co.developmentanddinosaurs.apps.poker.application.html.components

import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.header
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.li
import kotlinx.html.nav
import kotlinx.html.ul

fun FlowContent.header(page: String) =
    run {
        header {
            nav {
                div(classes = "nav-wrapper orange") {
                    a(classes = "brand-logo") {
                        href = "/"
                        img(classes = "logo-image") {
                            height = "64"
                            src = "/dinosaur-logo.png"
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
                    }
                }
            }
        }
    }
