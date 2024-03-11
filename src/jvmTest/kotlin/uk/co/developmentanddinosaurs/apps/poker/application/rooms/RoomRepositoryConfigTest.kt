package uk.co.developmentanddinosaurs.apps.poker.application.rooms

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class RoomRepositoryConfigTest : StringSpec({

    "Can read deletion delay from properties" {
        val config = RoomRepositoryConfig()

        config.deletionDelayMillis shouldBe 600000
    }
})
