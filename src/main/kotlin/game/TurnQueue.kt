package game

import entities.Entity
import entities.TurnTakingEntity
import java.util.*

class TurnQueue {

    private data class PrioritisedEntity(
        val entity: TurnTakingEntity,
        val heapKey: HeapKey
    )

    private data class HeapKey(
        var priority: Int,
        val unixTimestamp: Long
    )

    private val queue = PriorityQueue<PrioritisedEntity> { a, b ->
        val comparison = a.heapKey.priority.compareTo(b.heapKey.priority)
        if (comparison == 0) {
            a.heapKey.unixTimestamp.compareTo(b.heapKey.unixTimestamp)
        } else {
            comparison
        }
    }

    fun add(entity: TurnTakingEntity) {
        queue.add(
            PrioritisedEntity(
                heapKey = HeapKey(
                    priority = entity.speed,
                    unixTimestamp = Date().time
                ),
                entity = entity
            )
        )
    }

    fun add(entities: List<TurnTakingEntity>) {
        queue.addAll(
            entities.map { entity ->
                PrioritisedEntity(
                    heapKey = HeapKey(
                        priority = entity.speed,
                        unixTimestamp = Date().time
                    ),
                    entity = entity
                )
            }
        )
    }

    fun remove(entity: Entity) {
        queue.removeIf { inQueue ->
            inQueue.entity == entity
        }
    }

    fun poll() : TurnTakingEntity {
        val polled = queue.poll()
        queue.forEach { prioritisedEntity ->
            prioritisedEntity.heapKey.priority -= polled.heapKey.priority
        }
        return polled.entity
    }

    fun isNotEmpty() = queue.isNotEmpty()
}