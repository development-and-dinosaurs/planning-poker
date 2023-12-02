package uk.co.developmentanddinosaurs.apps.poker.infra

import software.amazon.awscdk.App

fun main() {
    val app = App()
    PokerStack(app, "PlanningPokerApplication")
    app.synth()
}
