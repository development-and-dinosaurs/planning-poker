import java.io.FileReader
import java.util.Properties

plugins {
    id("kotlin")
    id("com.diffplug.spotless")
}

group = "uk.co.developmentanddinosaurs.apps.poker"

repositories {
    mavenCentral()
}

dependencies {
    implementation("software.amazon.awscdk:aws-cdk-lib:2.131.0")
}

tasks {
    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes["Main-Class"] = "uk.co.developmentanddinosaurs.apps.poker.infra.CdkAppKt"
        }
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    }
    register<Exec>("uploadToS3") {
        val bucket = Properties().apply { load(FileReader("infra/src/main/resources/infra.properties")) }.getProperty("bucket-name")
        commandLine("aws", "s3", "cp", rootProject.tasks.jar.get().outputs.files.singleFile, "s3://$bucket/planning-poker.jar")
    }
}