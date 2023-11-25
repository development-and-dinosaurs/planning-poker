package uk.co.developmentanddinosaurs.apps.poker.application.html.components

import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.id

fun FlowContent.idButton(text: String) = run {
    a(classes = "waves-effect waves-light btn-large orange") {
        id = text.replace(" ", "-").lowercase()
        +text
    }
}

fun FlowContent.linkButton(text: String, link: String) = run {
    a(classes = "waves-effect waves-light btn-large orange") {
        href = link
        +text
    }
}
