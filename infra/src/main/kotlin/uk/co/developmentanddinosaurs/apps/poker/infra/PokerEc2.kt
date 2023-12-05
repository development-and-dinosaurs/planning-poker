package uk.co.developmentanddinosaurs.apps.poker.infra

import software.amazon.awscdk.services.ec2.Instance
import software.amazon.awscdk.services.ec2.InstanceClass
import software.amazon.awscdk.services.ec2.InstanceSize
import software.amazon.awscdk.services.ec2.InstanceType
import software.amazon.awscdk.services.ec2.LinuxUserDataOptions
import software.amazon.awscdk.services.ec2.MachineImage
import software.amazon.awscdk.services.ec2.Peer
import software.amazon.awscdk.services.ec2.Port
import software.amazon.awscdk.services.ec2.S3DownloadOptions
import software.amazon.awscdk.services.ec2.SecurityGroup
import software.amazon.awscdk.services.ec2.SubnetSelection
import software.amazon.awscdk.services.ec2.SubnetType
import software.amazon.awscdk.services.ec2.UserData
import software.amazon.awscdk.services.iam.Role
import software.amazon.awscdk.services.s3.Bucket
import software.constructs.Construct

class PokerEc2(private val scope: Construct) {
    fun instance(): Instance {
        return Instance.Builder.create(scope, "PokerInstance")
            .instanceType(InstanceType.of(InstanceClass.T2, InstanceSize.MICRO))
            .role(Role.fromRoleName(scope, "PlanningPokerApplication", "PlanningPokerApplication"))
            .machineImage(MachineImage.latestAmazonLinux2023()).vpc(PokerVpc.default(scope))
            .vpcSubnets(SubnetSelection.builder().subnetType(SubnetType.PUBLIC).build()).keyName("planning-poker")
            .securityGroup(securityGroup()).userData(userData()).associatePublicIpAddress(true).build()
    }

    private fun securityGroup(): SecurityGroup {
        val securityGroup =
            SecurityGroup.Builder.create(scope, "PokerSecurityGroup").securityGroupName("planning-poker-sg")
                .description("Planning Poker Security Group").vpc(PokerVpc.default(scope)).build()
        securityGroup.addIngressRule(Peer.anyIpv4(), Port.allTraffic())
        return securityGroup
    }

    private fun userData(): UserData {
        val userData = UserData.forLinux(LinuxUserDataOptions.builder().shebang("#!/bin/bash").build())
        userData.addCommands("sudo yum install -y java-17-amazon-corretto-headless")
        userData.addS3DownloadCommand(download("planning-poker-application", "planning-poker-jvm-1.0.3.jar"))
        userData.addCommands("sudo java -jar /app/planning-poker-jvm-1.0.3.jar &")
        return userData
    }

    private fun download(bucket: String, key: String): S3DownloadOptions {
        return S3DownloadOptions.builder().bucket(Bucket.fromBucketName(scope, "bucket", bucket)).localFile("/app/$key")
            .bucketKey(key).build()
    }
}
