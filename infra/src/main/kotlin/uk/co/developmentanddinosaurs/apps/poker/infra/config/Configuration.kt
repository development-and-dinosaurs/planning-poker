package uk.co.developmentanddinosaurs.apps.poker.infra.config

import java.util.Properties

object Configuration {
    private val properties by lazy { Properties().apply { load(Configuration::class.java.getResourceAsStream("/infra.properties")) } }

    val awsAccount: String =
        properties.getProperty("aws-account")
            ?: throw ConfigException("No AWS account found. Specify aws-account in infra.properties")
    val awsRegion: String =
        properties.getProperty("aws-region")
            ?: throw ConfigException("No AWS region found. Specify aws-region in infra.properties")
    val bucketName: String =
        properties.getProperty("bucket-name")
            ?: throw ConfigException("No bucket name found. Specify bucket-name in infra.properties")
    val githubOrganization: String =
        properties.getProperty("github-organization")
            ?: throw ConfigException("No GitHub organization found. Specify github-organization in infra.properties")
    val githubRepository: String =
        properties.getProperty("github-repository")
            ?: throw ConfigException("No GitHub repository found. Specify github-repository in infra.properties")
    val jar: String = Configuration::class.java.getResourceAsStream("/jar-to-upload").bufferedReader().readText()
}
