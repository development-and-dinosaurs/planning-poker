package uk.co.developmentanddinosaurs.apps.poker.infra

import software.amazon.awscdk.Environment
import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.amazon.awscdk.services.ec2.CfnEIPAssociation
import software.amazon.awscdk.services.ec2.CfnEIPAssociationProps
import software.constructs.Construct

class PokerStack(parent: Construct, name: String) : Stack(parent, name, envProperties()) {
    init {
        val instance = PokerEc2(scope = this).instance()
        val eip = PokerEip(scope = this).eip()
        CfnEIPAssociation(this, "eip", CfnEIPAssociationProps.builder().instanceId(instance.instanceId).allocationId(eip.attrAllocationId).build())
    }
}

fun envProperties(): StackProps =
    StackProps.builder().env(Environment.builder().account("642731005123").region("eu-west-2").build()).build()
