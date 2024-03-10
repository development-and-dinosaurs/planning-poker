package uk.co.developmentanddinosaurs.apps.poker.infra.deployment

import software.amazon.awscdk.Stack
import software.constructs.Construct
import uk.co.developmentanddinosaurs.apps.poker.infra.common.PokerBucket
import uk.co.developmentanddinosaurs.apps.poker.infra.envProperties

/**
 * Stack to deploy the deployment infrastructure to AWS.
 *
 * This creates the infrastructure required to deploy the Poker application, such as a deployment
 * IAM role, and an S3 bucket to hold the application executable.
 */
class PokerDeploymentStack(parent: Construct, name: String) : Stack(parent, name, envProperties()) {
    init {
        val role = PokerDeploymentIamRole(scope = this).role()
        val bucket = PokerBucket(scope = this).bucket()
        bucket.grantPut(role)
    }
}
