package uk.co.developmentanddinosaurs.apps.poker.infra.application

import software.amazon.awscdk.services.ec2.Peer
import software.amazon.awscdk.services.ec2.Port
import software.amazon.awscdk.services.ec2.SecurityGroup
import software.constructs.Construct

class PokerSecurityGroup(private val scope: Construct) {
    fun securityGroup(): SecurityGroup {
        val securityGroup =
            SecurityGroup.Builder.create(scope, "PokerSecurityGroup").securityGroupName("planning-poker-sg")
                .description("Planning Poker Security Group").vpc(PokerVpc.default(scope)).build()
        securityGroup.addIngressRule(Peer.anyIpv4(), Port.allTraffic())
        return securityGroup
    }
}
