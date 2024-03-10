package uk.co.developmentanddinosaurs.apps.poker.infra.application

import software.amazon.awscdk.services.iam.Role
import software.amazon.awscdk.services.iam.ServicePrincipal
import software.constructs.Construct

class PokerAppIamRole(private val scope: Construct) {
    fun role(): Role {
        return Role.Builder.create(scope, "PokerRole")
            .roleName("PokerApplication")
            .description("IAM role for the Planning Poker Application")
            .assumedBy(ServicePrincipal("ec2.amazonaws.com"))
            .build()
    }
}
