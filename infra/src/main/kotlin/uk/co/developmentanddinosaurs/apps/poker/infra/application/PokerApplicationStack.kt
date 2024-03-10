package uk.co.developmentanddinosaurs.apps.poker.infra.application

import software.amazon.awscdk.Stack
import software.amazon.awscdk.services.ec2.CfnEIPAssociation
import software.amazon.awscdk.services.ec2.CfnEIPAssociationProps
import software.constructs.Construct
import uk.co.developmentanddinosaurs.apps.poker.infra.common.PokerBucket
import uk.co.developmentanddinosaurs.apps.poker.infra.envProperties

class PokerApplicationStack(parent: Construct, name: String) : Stack(parent, name, envProperties()) {
    init {
        val instance = PokerEc2(scope = this).instance()
        val eip = PokerEip(scope = this).eip()
        val role = PokerAppIamRole(scope = this).role()
        PokerBucket(scope = this).bucketReference().grantRead(role)
        CfnEIPAssociation(
            this,
            "eip",
            CfnEIPAssociationProps.builder().instanceId(instance.instanceId).allocationId(eip.attrAllocationId).build(),
        )
    }
}
