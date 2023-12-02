package uk.co.developmentanddinosaurs.apps.poker.infra

import software.amazon.awscdk.services.s3.Bucket
import software.amazon.awscdk.services.s3.BucketAttributes
import software.amazon.awscdk.services.s3.IBucket
import software.constructs.Construct

class PokerBucket(private val scope: Construct) {

    fun bucket() : IBucket {
        return Bucket.fromBucketAttributes(scope, "PokerBucket", BucketAttributes.builder().bucketName("planning-poker-application").build())
    }
}