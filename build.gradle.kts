import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl
import java.io.FileReader
import java.util.Properties

plugins {
    application
    id("com.diffplug.spotless") version "6.25.0"
    kotlin("multiplatform") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
    id("uk.co.developmentanddinosaurs.git-versioner") version "2.0.1"
}

group = "uk.co.developmentanddinosaurs.apps.poker"

repositories {
    mavenCentral()
}

kotlin {
    js(IR) {
        binaries.executable()
        browser {
            @OptIn(ExperimentalDistributionDsl::class)
            distribution {
                outputDirectory = file("$projectDir/build/processedResources/jvm/main/web")
            }
            commonWebpackConfig {
                cssSupport {
                    enabled = true
                }
            }
        }
    }
    jvm {
        jvmToolchain(17)
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val ktorVersion = "2.3.11"
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            }
        }
        val commonTest by getting
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-js")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.8.1")
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("io.ktor:ktor-client-websockets:$ktorVersion")
            }
        }
        val jsTest by getting
        val jvmMain by getting {
            dependencies {
                implementation("ch.qos.logback:logback-classic:1.5.6")
                implementation("io.ktor:ktor-network-tls-certificates:$ktorVersion")
                implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
                implementation("io.ktor:ktor-server-default-headers:$ktorVersion")
                implementation("io.ktor:ktor-server-html-builder:$ktorVersion")
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-server-sessions:$ktorVersion")
                implementation("io.ktor:ktor-server-status-pages-jvm:$ktorVersion")
                implementation("io.ktor:ktor-server-websockets:$ktorVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.729")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("io.kotest:kotest-runner-junit5:5.8.1")
                implementation("io.mockk:mockk:1.13.10")
            }
        }
    }
}

versioner {
    startFrom {
        major = 1
    }
    git {
        authentication {
            https {
                token = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

application {
    mainClass.set("uk.co.developmentanddinosaurs.apps.poker.application.PokerAppKt")
}

spotless {
    kotlin {
        target("src/**/*.kt", "infra/src/**/*.kt")
        ktlint()
    }
    kotlinGradle {
        ktlint()
    }
    freshmark {
        target("**/*.md")
    }
    yaml {
        target("**/*.yml")
        prettier()
    }
}

tasks {
    named<Copy>("jvmProcessResources") {
        from(named("jsBrowserDistribution"))
    }
    named<JavaExec>("run") {
        dependsOn(named("jvmJar"))
        classpath(named("jvmJar"))
    }
    named<Jar>("jvmJar") {
        dependsOn(named("writeProjectVersion"))
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes["Main-Class"] = "uk.co.developmentanddinosaurs.apps.poker.application.PokerAppKt"
        }
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    }
    register<Exec>("uploadToS3") {
        dependsOn(jar)
        val bucket = Properties().apply { load(FileReader("infra/src/main/resources/infra.properties")) }.getProperty("bucket-name")
        commandLine("aws", "s3", "cp", jar.get().outputs.files.singleFile, "s3://$bucket")
    }
    register("writeProjectVersion") {
        doLast {
            val content = project.version.toString()
            val output = File(project.layout.buildDirectory.get().asFile, "processedResources/jvm/main/version")
            output.parentFile.mkdirs()
            output.writeText(content)
        }
    }
}
