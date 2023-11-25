package uk.co.developmentanddinosaurs.apps.poker.application.services

class NameGenerator {
    private val adjectives =
        NameGenerator::class.java.getResource("/adjectives.txt").readText().split(System.lineSeparator())
    private val dinosaurs =
        NameGenerator::class.java.getResource("/dinosaurs.txt").readText().split(System.lineSeparator())

    fun generateName(): String {
        return adjectives.random() + " " + dinosaurs.random()
    }
}
