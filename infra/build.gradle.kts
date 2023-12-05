plugins {
    id("kotlin")
    id("com.diffplug.spotless") version "6.23.0"
}

group = "uk.co.developmentanddinosaurs.apps.poker"
version = "1.0.4"

repositories {
    mavenCentral()
}

dependencies {
    implementation("software.amazon.awscdk:aws-cdk-lib:2.111.0")
}

tasks {
    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes["Main-Class"] = "uk.co.developmentanddinosaurs.apps.poker.infra.MainKt"
        }
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    }
}