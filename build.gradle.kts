plugins {
    application
    id("com.diffplug.spotless") version "6.25.0"
    kotlin("multiplatform") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.22"
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
            distribution(
                Action {
                    outputDirectory = file("$projectDir/build/processedResources/jvm/main/web")
                },
            )
            commonWebpackConfig(
                Action {
                    cssSupport {
                        enabled.set(true)
                    }
                },
            )
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
        val ktorVersion = "2.3.8"
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            }
        }
        val commonTest by getting
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-js")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.8.0")
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("io.ktor:ktor-client-websockets:$ktorVersion")
            }
        }
        val jsTest by getting
        val jvmMain by getting {
            dependencies {
                implementation("ch.qos.logback:logback-classic:1.5.2")
                implementation("io.ktor:ktor-network-tls-certificates:$ktorVersion")
                implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
                implementation("io.ktor:ktor-server-default-headers:$ktorVersion")
                implementation("io.ktor:ktor-server-html-builder:$ktorVersion")
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-server-sessions:$ktorVersion")
                implementation("io.ktor:ktor-server-status-pages-jvm:$ktorVersion")
                implementation("io.ktor:ktor-server-websockets:$ktorVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.708")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("io.kotest:kotest-runner-junit5:5.8.0")
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
        target("src/**/*.kt")
        ktlint()
    }
    kotlinGradle {
        ktlint()
    }
    freshmark {
        target("**/*.md")
    }
}

tasks.named<Copy>("jvmProcessResources") {
    val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
    from(jsBrowserDistribution)
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}

tasks.named<Jar>("jvmJar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveFileName = "${project.name}.jar"
    manifest {
        attributes["Main-Class"] = "uk.co.developmentanddinosaurs.apps.poker.application.PokerAppKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
