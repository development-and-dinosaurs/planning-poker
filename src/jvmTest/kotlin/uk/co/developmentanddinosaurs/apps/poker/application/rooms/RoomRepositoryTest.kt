package uk.co.developmentanddinosaurs.apps.poker.application.rooms

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import uk.co.developmentanddinosaurs.apps.poker.application.services.NameGenerator

class RoomRepositoryTest : StringSpec({
    val nameGenerator = mockk<NameGenerator>()
    var roomRepository = RoomRepository(nameGenerator)

    beforeEach {
        every { nameGenerator.generateName() } returnsMany listOf("TestRoom", "TestRoom", "TestRoom2")
        roomRepository = RoomRepository(nameGenerator)
    }

    "Can create room" {
        val room = roomRepository.createRoom()

        room.id shouldBe "TestRoom"
    }

    "Can get room" {
        val room = roomRepository.createRoom()

        val retrievedRoom = roomRepository.getRoom(room.id)

        retrievedRoom shouldBe room
    }

    "Regenerates name when name clashes with existing room" {
        roomRepository.createRoom()

        val room = roomRepository.createRoom()

        room.id shouldBe "TestRoom2"
    }

    "Throws exception when room does not exist" {
        val exception =
            shouldThrow<RoomDoesNotExistException> {
                roomRepository.getRoom("NonExistentRoom")
            }
        exception shouldHaveMessage "NonExistentRoom does not exist"
    }
})
