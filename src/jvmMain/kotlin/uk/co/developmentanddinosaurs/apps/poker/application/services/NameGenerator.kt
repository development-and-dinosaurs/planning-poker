package uk.co.developmentanddinosaurs.apps.poker.application.services

class NameGenerator {
    private val adjectives = NameGenerator::class.java.getResource("/adjectives.txt").readText().split("\r\n")
    private val dinosaurs = NameGenerator::class.java.getResource("/dinosaurs.txt").readText().split("\r\n")
    fun generateName(): String {
        return adjectives.random() + " " + dinosaurs.random()
    }
}
