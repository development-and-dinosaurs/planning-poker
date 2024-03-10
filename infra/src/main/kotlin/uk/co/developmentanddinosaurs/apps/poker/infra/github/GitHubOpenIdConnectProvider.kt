package uk.co.developmentanddinosaurs.apps.poker.infra.github

import software.amazon.awscdk.services.iam.IOpenIdConnectProvider
import software.amazon.awscdk.services.iam.OpenIdConnectProvider
import software.constructs.Construct
import uk.co.developmentanddinosaurs.apps.poker.infra.config.Configuration

/**
 * OpenIdConnectProvider for GitHub Actions.
 */
class GitHubOpenIdConnectProvider(private val scope: Construct) {
    /**
     * Creates the [OpenIdConnectProvider] for GitHub Actions.
     *
     * See https://docs.github.com/en/actions/deployment/security-hardening-your-deployments/configuring-openid-connect-in-amazon-web-services
     */
    fun provider(): OpenIdConnectProvider {
        return OpenIdConnectProvider.Builder.create(scope, "GitHubOpenIdConnectProvider")
            .url("https://token.actions.githubusercontent.com")
            .thumbprints(listOf("6938fd4d98bab03faadb97b34396831e3780aea1"))
            .clientIds(listOf("sts.amazonaws.com"))
            .build()
    }

    /**
     * Returns the previously created [OpenIdConnectProvider] from [provider].
     *
     * @return [IOpenIdConnectProvider]
     */
    fun providerFromArn(): IOpenIdConnectProvider {
        return OpenIdConnectProvider.fromOpenIdConnectProviderArn(
            scope,
            "GitHubConnectProvider",
            "arn:aws:iam::${Configuration.awsAccount}:oidc-provider/token.actions.githubusercontent.com",
        )
    }
}
