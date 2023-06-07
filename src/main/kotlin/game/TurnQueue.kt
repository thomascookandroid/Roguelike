package game

import state.actions.Action
import components.Queueable
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import state.LocalMapState
import java.util.*

@Serializable
class TurnQueue {
    @Serializable
    private data class PrioritisedQueueable(
        val queueable: Queueable,
        val heapKey: HeapKey
    )

    @Serializable
    private data class HeapKey(
        var priority: Int,
        val unixTimestamp: Long
    )

    @Transient
    private val queue = PriorityQueue<PrioritisedQueueable> { a, b ->
        val comparison = a.heapKey.priority.compareTo(b.heapKey.priority)
        if (comparison == 0) {
            a.heapKey.unixTimestamp.compareTo(b.heapKey.unixTimestamp)
        } else {
            comparison
        }
    }

    fun add(
        queueable: Queueable
    ) {
        queue.add(
            PrioritisedQueueable(
                heapKey = HeapKey(
                    priority = queueable.speed,
                    unixTimestamp = Date().time
                ),
                queueable = queueable
            )
        )
    }

    fun add(
        queueables: List<Queueable>
    ) {
        queue.addAll(
            queueables.map { queueable ->
                PrioritisedQueueable(
                    heapKey = HeapKey(
                        priority = queueable.speed,
                        unixTimestamp = Date().time
                    ),
                    queueable = queueable
                )
            }
        )
    }

    fun remove(
        queueable: Queueable
    ) {
        queue.removeIf { inQueue ->
            inQueue.queueable == queueable
        }
    }

    tailrec suspend fun takeTurn(
        localMapState: LocalMapState
    ) {
        val action = queue.peek().queueable.getAction(
            localMapState = localMapState
        )
        action.run()
        when (action) {
            is Action.Terminal -> finishTurn()
            else -> takeTurn(
                localMapState = localMapState
            )
        }
    }

    private fun finishTurn() {
        val polled = queue.poll()
        queue.forEach { prioritisedQueueable ->
            prioritisedQueueable.heapKey.priority -= polled.heapKey.priority
        }
        add(polled.queueable)
    }

    fun isNotEmpty() = queue.isNotEmpty()
}