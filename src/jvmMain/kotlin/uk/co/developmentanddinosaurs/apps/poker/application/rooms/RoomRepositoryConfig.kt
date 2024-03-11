package uk.co.developmentanddinosaurs.apps.poker.application.rooms

import java.util.Properties

class RoomRepositoryConfig {
    private val properties by lazy {
        Properties().apply { load(RoomRepositoryConfig::class.java.getResourceAsStream("/application.properties")) }
    }

    val deletionDelayMillis: Long = properties.getProperty("room-repository-deletion-delay-millis").toLong()
}
