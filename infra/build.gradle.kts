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
            attributes["Main-Class"] = "uk.co.developmentanddinosaurs.apps.poker.infra.MainKt"
        }
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    }
}