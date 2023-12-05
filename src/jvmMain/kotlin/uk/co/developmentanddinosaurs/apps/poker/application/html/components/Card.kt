package uk.co.developmentanddinosaurs.apps.poker.application.html.components

import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.span

fun FlowContent.card(card: DinosaurCard) =
    run {
        div(classes = "col s4 m2 l2") {
            div(classes = "card") {
                id = "card-${card.size}"
                div(classes = "card-image") {
                    img(classes = "points-image") {
                        alt = card.alt
                        src = card.image
                    }
                    span(classes = "dino-card-title") {
                        +"${card.size}"
                    }
                }
            }
        }
    }

enum class DinosaurCard(val size: Int) {
    DINOSAUR_EGG(1),
    DILOPHOSAURUS(2),
    VELOCIRAPTOR(3),
    TRICERATOPS(5),
    TYRANNOSAURUS_REX(8),
    METEOR(13),
    ;

    val image = "/images/cards/dinosaur/dinosaur-$size.png"
    val alt = "${name.lowercase().replaceFirstChar { it.uppercase() }.replace("_", " ")} Card"
}
