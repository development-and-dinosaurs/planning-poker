plugins {
    id("kotlin")
    id("com.diffplug.spotless")
}

group = "uk.co.developmentanddinosaurs.apps.poker"

repositories {
    mavenCentral()
}

dependencies {
    implementation("software.amazon.awscdk:aws-cdk-lib:2.145.0")
}

tasks {
    jar {
        dependsOn("writeProjectJarFilename")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes["Main-Class"] = "uk.co.developmentanddinosaurs.apps.poker.infra.CdkAppKt"
        }
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    }
    register("writeProjectJarFilename") {
        doLast {
            val content = rootProject.tasks.jar.get().outputs.files.singleFile.name
            val output = File(project.layout.buildDirectory.get().asFile, "resources/main/jar-to-upload")
            output.parentFile.mkdirs()
            output.writeText(content)
        }
    }
}
