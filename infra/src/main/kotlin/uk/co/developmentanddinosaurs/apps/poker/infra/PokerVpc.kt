package uk.co.developmentanddinosaurs.apps.poker.infra

import software.amazon.awscdk.services.ec2.IVpc
import software.amazon.awscdk.services.ec2.VpcLookupOptions
import software.constructs.Construct
import software.amazon.awscdk.services.ec2.Vpc as AwsVpc

class PokerVpc {
    companion object {
        private var vpc: IVpc? = null
        fun default(construct: Construct): IVpc {
            if (vpc != null) return vpc!!
            vpc = AwsVpc.fromLookup(construct, "DefaultVpc", VpcLookupOptions.builder().isDefault(true).build())
            return vpc!!
        }
    }
}