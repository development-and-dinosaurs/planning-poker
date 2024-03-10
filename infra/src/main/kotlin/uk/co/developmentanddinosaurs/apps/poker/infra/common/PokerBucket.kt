package uk.co.developmentanddinosaurs.apps.poker.infra.common

import software.amazon.awscdk.services.s3.BlockPublicAccess
import software.amazon.awscdk.services.s3.Bucket
import software.amazon.awscdk.services.s3.BucketAccessControl
import software.amazon.awscdk.services.s3.BucketEncryption
import software.amazon.awscdk.services.s3.IBucket
import software.amazon.awscdk.services.s3.ObjectOwnership
import software.constructs.Construct
import uk.co.developmentanddinosaurs.apps.poker.infra.config.Configuration

/**
 * S3 bucket that contains the Poker application.
 */
class PokerBucket(private val scope: Construct) {

    /**
     * Creates a [Bucket] that is used to store the Poker application.
     *
     * @return the bucket
     */
    fun bucket(): Bucket {
        return Bucket.Builder.create(scope, "PokerBucket")
            .bucketName(Configuration.bucketName)
            .accessControl(BucketAccessControl.BUCKET_OWNER_FULL_CONTROL)
            .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
            .objectOwnership(ObjectOwnership.BUCKET_OWNER_ENFORCED)
            .encryption(BucketEncryption.S3_MANAGED)
            .build()
    }

    /**
     * Returns a reference to the [Bucket] created in [bucket].
     *
     * @return previously created bucket
     */
    fun bucketReference(): IBucket {
        if (bucket == null) {
            bucket = Bucket.fromBucketName(scope, "PokerBucketReference", Configuration.bucketName)
        }
        return bucket!!
    }

    companion object Cache {
        private var bucket: IBucket? = null
    }
}
