package uk.co.developmentanddinosaurs.apps.poker.infra

import software.amazon.awscdk.App
import uk.co.developmentanddinosaurs.apps.poker.infra.application.PokerApplicationStack
import uk.co.developmentanddinosaurs.apps.poker.infra.deployment.PokerDeploymentStack
import uk.co.developmentanddinosaurs.apps.poker.infra.github.GitHubOidcStack

fun main() {
    val cdkApp = App()
    GitHubOidcStack(cdkApp, "GitHubOidc")
    PokerDeploymentStack(cdkApp, "PokerDeployment")
    PokerApplicationStack(cdkApp, "PokerApplication")
    cdkApp.synth()
}
