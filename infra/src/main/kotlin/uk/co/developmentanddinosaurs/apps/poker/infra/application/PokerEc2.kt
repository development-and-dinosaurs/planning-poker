package uk.co.developmentanddinosaurs.apps.poker.infra.application

import software.amazon.awscdk.services.ec2.Instance
import software.amazon.awscdk.services.ec2.InstanceClass
import software.amazon.awscdk.services.ec2.InstanceSize
import software.amazon.awscdk.services.ec2.InstanceType
import software.amazon.awscdk.services.ec2.KeyPair
import software.amazon.awscdk.services.ec2.MachineImage
import software.amazon.awscdk.services.ec2.MultipartUserData
import software.amazon.awscdk.services.ec2.S3DownloadOptions
import software.amazon.awscdk.services.ec2.SubnetSelection
import software.amazon.awscdk.services.ec2.SubnetType
import software.amazon.awscdk.services.ec2.UserData
import software.amazon.awscdk.services.iam.Role
import software.amazon.awscdk.services.s3.Bucket
import software.constructs.Construct
import uk.co.developmentanddinosaurs.apps.poker.infra.common.PokerBucket
import uk.co.developmentanddinosaurs.apps.poker.infra.config.Configuration

class PokerEc2(private val scope: Construct) {
    fun instance(): Instance {
        return Instance.Builder.create(scope, "PokerInstance")
            .instanceType(InstanceType.of(InstanceClass.T2, InstanceSize.MICRO))
            .role(Role.fromRoleName(scope, "PlanningPokerApplication", "PlanningPokerApplication"))
            .machineImage(MachineImage.latestAmazonLinux2023()).vpc(PokerVpc.default(scope))
            .vpcSubnets(SubnetSelection.builder().subnetType(SubnetType.PUBLIC).build()).keyPair(keyPair())
            .securityGroup(PokerSecurityGroup(scope).securityGroup()).userData(userData())
            .associatePublicIpAddress(true).build()
    }

    private fun keyPair(): KeyPair {
        return KeyPair.Builder.create(scope, "PlanningPokerKeyPair")
            .keyPairName("planning-poker")
            .build()
    }

    private fun userData(): UserData {
        val initConfig = UserData.forLinux()
        initConfig.addCommands("#cloud-config")
        initConfig.addCommands("cloud_final_modules:")
        initConfig.addCommands("- [scripts-user, always]")
        val userData = UserData.forLinux()
        userData.addCommands("sudo yum install -y java-17-amazon-corretto-headless")
        userData.addS3DownloadCommand(download(PokerBucket(scope).bucketReference().bucketName, Configuration.jar))
        userData.addCommands("sudo java -jar /app/planning-poker.jar &")
        val multipartUserData = MultipartUserData()
        multipartUserData.addUserDataPart(initConfig)
        multipartUserData.addUserDataPart(userData)
        return multipartUserData
    }

    private fun download(
        bucket: String,
        key: String,
    ): S3DownloadOptions {
        return S3DownloadOptions.builder().bucket(Bucket.fromBucketName(scope, "bucket", bucket))
            .localFile("/app/planning-poker.jar")
            .bucketKey(key).build()
    }
}
