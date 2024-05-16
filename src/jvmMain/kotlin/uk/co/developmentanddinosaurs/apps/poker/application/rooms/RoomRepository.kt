package uk.co.developmentanddinosaurs.apps.poker.application.rooms

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uk.co.developmentanddinosaurs.apps.poker.application.observability.Observability
import uk.co.developmentanddinosaurs.apps.poker.application.services.NameGenerator

class RoomRepository(
    private val nameGenerator: NameGenerator,
    private val config: RoomRepositoryConfig,
) {
    private val rooms = mutableMapOf<String, Room>()
    private val roomsPendingRemoval = mutableMapOf<String, Job>()

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
        if (roomsPendingRemoval.containsKey(roomId)) {
            roomsPendingRemoval[roomId]?.cancel()
            Observability.roomDeletionCancelled(roomId)
        }
        return rooms[roomId] ?: throw RoomDoesNotExistException(roomId)
    }

    fun removeRoom(roomId: String) {
        roomsPendingRemoval[roomId] =
            CoroutineScope(Dispatchers.Default).launch {
                Observability.roomDeletionScheduled(roomId)
                delay(config.deletionDelayMillis)
                rooms.remove(roomId)
                Observability.roomDeleted(roomId)
            }
    }
}

class RoomDoesNotExistException(val room: String) : RuntimeException("$room does not exist")
