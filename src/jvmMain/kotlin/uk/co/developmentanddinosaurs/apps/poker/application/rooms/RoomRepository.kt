package uk.co.developmentanddinosaurs.apps.poker.application.rooms

import uk.co.developmentanddinosaurs.apps.poker.application.services.NameGenerator

class RoomRepository(private val nameGenerator: NameGenerator) {

    private val rooms = mutableMapOf<String, Room>("Test" to Room("Test"))

    fun createRoom(): Room {
        val name = generateUniqueName()
        val room = Room(name)
        rooms[room.id] = room
        return room
    }

    private fun generateUniqueName(): String {
        var name = nameGenerator.generateName().replace(" ", "")
        while (rooms.containsKey(name)) {
            name = nameGenerator.generateName().replace(" ", "")
        }
        return name
    }

    fun getRoom(roomId: String): Room {
        return rooms[roomId] ?: throw RoomDoesNotExistException(roomId)
    }

}

class RoomDoesNotExistException(room: String) : RuntimeException("$room does not exist")
