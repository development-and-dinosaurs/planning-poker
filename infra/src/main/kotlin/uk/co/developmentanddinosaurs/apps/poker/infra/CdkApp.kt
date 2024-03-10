package uk.co.developmentanddinosaurs.apps.poker.infra

import software.amazon.awscdk.App

fun main() {
    val cdkApp = App()
    PokerStack(app, "PlanningPokerApplication")
    cdkApp.synth()
}
