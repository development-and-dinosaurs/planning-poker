package uk.co.developmentanddinosaurs.apps.poker.application.services

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeIn
import java.io.File

class NameGeneratorTest : StringSpec({
    val nameGenerator = NameGenerator()

    val adjectives = File("src/jvmMain/resources/adjectives.txt").readLines()
    val dinosaurs = File("src/jvmMain/resources/dinosaurs.txt").readLines()

    "Generates name from adjective and dinosaur files" {
        val name = nameGenerator.generateName()

        val adjective = name.split(" ")[0]
        val dinosaur = name.split(" ")[1]

        adjective shouldBeIn adjectives
        dinosaur shouldBeIn dinosaurs
    }
})
