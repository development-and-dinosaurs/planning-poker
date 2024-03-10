package uk.co.developmentanddinosaurs.apps.poker.infra.github

import software.amazon.awscdk.Stack
import software.constructs.Construct
import uk.co.developmentanddinosaurs.apps.poker.infra.envProperties

/**
 * Stack to deploy the OpenIdConnectProvider for GitHub to AWS.
 *
 * This will allow us to assume a role from our GitHub actions to deploy our infrastructure.
 *
 * As this is the stack that gives GitHub access, it must be deployed outside of GitHub actions.
 */
class GitHubOidcStack(parent: Construct, name: String) : Stack(parent, name, envProperties()) {
    init {
        GitHubOpenIdConnectProvider(scope = this).provider()
    }
}
