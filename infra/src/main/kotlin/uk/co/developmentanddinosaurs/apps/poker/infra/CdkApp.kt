package uk.co.developmentanddinosaurs.apps.poker.infra

import software.amazon.awscdk.App
import uk.co.developmentanddinosaurs.apps.poker.infra.github.GitHubOidcStack

fun main() {
    val cdkApp = App()
    GitHubOidcStack(cdkApp, "GitHubOidc")
    cdkApp.synth()
}
