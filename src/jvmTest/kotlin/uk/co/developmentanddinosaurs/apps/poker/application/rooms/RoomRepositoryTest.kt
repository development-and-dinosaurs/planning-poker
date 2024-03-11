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
    val config = mockk<RoomRepositoryConfig>()
    var roomRepository = RoomRepository(nameGenerator, config)

    beforeEach {
        every { nameGenerator.generateName() } returnsMany listOf("TestRoom", "TestRoom", "TestRoom2")
        every { config.deletionDelayMillis } returns 1
        roomRepository = RoomRepository(nameGenerator, config)
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

    "Removes room after a delay" {
        val room = roomRepository.createRoom()

        roomRepository.removeRoom(room.id)
        Thread.sleep(10)

        shouldThrow<RoomDoesNotExistException> { roomRepository.getRoom(room.id) }
    }

    "Room still exists before delayed deletion" {
        val room = roomRepository.createRoom()

        roomRepository.removeRoom(room.id)

        val retrievedRoom = roomRepository.getRoom(room.id)
        retrievedRoom shouldBe room
    }

    "Retrieving a room pending deletion cancels deletion of the room" {
        val room = roomRepository.createRoom()
        roomRepository.removeRoom(room.id)

        roomRepository.getRoom(room.id)

        Thread.sleep(10)
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
