package uk.co.developmentanddinosaurs.apps.poker.infra.deployment

import software.amazon.awscdk.services.iam.ManagedPolicy
import software.amazon.awscdk.services.iam.OpenIdConnectPrincipal
import software.amazon.awscdk.services.iam.Role
import software.constructs.Construct
import uk.co.developmentanddinosaurs.apps.poker.infra.config.Configuration
import uk.co.developmentanddinosaurs.apps.poker.infra.github.GitHubOpenIdConnectProvider

/**
 * IAM role to use for deploying the Poker application.
 *
 * This role is used to deploy the Poker application infrastructure, which can be assumed by GitHub Actions.
 *
 * This is the last thing we need to deploy manually before allowing GitHub Actions access to our infrastructure.
 */
class PokerDeploymentIamRole(private val scope: Construct) {

    /**
     * Creates the deployment IAM role.
     *
     * This role can only be assumed by GitHub Actions working in the correct organization and repository,
     * as specified in the [Configuration] class.
     *
     * @return the deployment IAM role
     */
    fun role(): Role {
        return Role.Builder.create(scope, "PokerDeploymentIamRole")
            .roleName("PokerDeployment")
            .assumedBy(
                OpenIdConnectPrincipal(GitHubOpenIdConnectProvider(scope).providerFromArn())
                    .withConditions(
                        mapOf(
                            "StringEquals" to
                                mapOf(
                                    "token.actions.githubusercontent.com:aud" to "sts.amazonaws.com",
                                ),
                            "StringLike" to
                                mapOf(
                                    "token.actions.githubusercontent.com:sub" to
                                        "repo:${Configuration.githubOrganization}/${Configuration.githubRepository}*",
                                ),
                        ),
                    ),
            )
            .managedPolicies(
                listOf(
                    ManagedPolicy.fromManagedPolicyArn(
                        scope,
                        "Admin",
                        "arn:aws:iam::aws:policy/AdministratorAccess",
                    ),
                ),
            )
            .build()
    }
}
