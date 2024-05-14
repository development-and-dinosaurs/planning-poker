package uk.co.developmentanddinosaurs.apps.poker.application

object Configuration {
    val version = Configuration::class.java.getResourceAsStream("/version").bufferedReader().readText()
}
