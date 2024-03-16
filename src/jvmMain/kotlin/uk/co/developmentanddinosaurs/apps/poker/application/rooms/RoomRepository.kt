package uk.co.developmentanddinosaurs.apps.poker.application.rooms

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import uk.co.developmentanddinosaurs.apps.poker.application.services.NameGenerator
import java.time.Instant

class RoomRepository(
    private val nameGenerator: NameGenerator,
    private val config: RoomRepositoryConfig,
) {
    private val rooms = mutableMapOf<String, Room>()
    private val roomsPendingRemoval = mutableMapOf<String, Job>()

    private val log = LoggerFactory.getLogger(javaClass)

    fun createRoom(): Room {
        val name = generateUniqueName()
        val room = Room(name)
        rooms[room.id] = room
        log.info("Created room [$name]")
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
            log.info("Cancelling scheduled deletion of [$roomId]")
            roomsPendingRemoval[roomId]?.cancel()
        }
        return rooms[roomId] ?: throw RoomDoesNotExistException(roomId)
    }

    fun removeRoom(roomId: String) {
        roomsPendingRemoval[roomId] =
            CoroutineScope(Dispatchers.Default).launch {
                log.info("Room [$roomId] scheduled for deletion at ${Instant.now().plusMillis(config.deletionDelayMillis)}")
                delay(config.deletionDelayMillis)
                rooms.remove(roomId)
                log.info("Room [$roomId] deleted")
            }
    }
}

class RoomDoesNotExistException(val room: String) : RuntimeException("$room does not exist")
