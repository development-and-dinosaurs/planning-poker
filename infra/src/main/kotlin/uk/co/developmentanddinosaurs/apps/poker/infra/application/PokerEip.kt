package uk.co.developmentanddinosaurs.apps.poker.infra.application

import software.amazon.awscdk.services.ec2.CfnEIP
import software.constructs.Construct

class PokerEip(private val scope: Construct) {
    fun eip(): CfnEIP {
        return CfnEIP(scope, "PokerEip")
    }
}
