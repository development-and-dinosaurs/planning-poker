package uk.co.developmentanddinosaurs.apps.poker.infra

import software.amazon.awscdk.Environment
import software.amazon.awscdk.StackProps
import uk.co.developmentanddinosaurs.apps.poker.infra.config.Configuration

fun envProperties(): StackProps =
    StackProps.builder()
        .env(Environment.builder().account(Configuration.awsAccount).region(Configuration.awsRegion).build()).build()
