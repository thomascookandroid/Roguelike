package game

import actions.Action
import components.Queueable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import state.LocalMapState
import state.State
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

    suspend fun takeTurn(
        scope: CoroutineScope,
        localMapState: LocalMapState
    ) {
        val action = queue.peek().queueable.getAction(
            localMapState = localMapState
        ).apply {
            run(
                scope = scope
            ).join()
        }
        when (action) {
            is Action.Terminal -> {
                advance()
            }
            else -> takeTurn(
                scope = scope,
                localMapState = localMapState
            )
        }
    }

    private fun advance() {
        val polled = queue.poll()
        queue.forEach { prioritisedQueueable ->
            prioritisedQueueable.heapKey.priority -= polled.heapKey.priority
        }
        add(polled.queueable)
    }

    fun isNotEmpty() = queue.isNotEmpty()
}