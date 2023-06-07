package algorithms

import components.Positionable
import data.Position
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.*
import java.util.concurrent.Executors

class EntityPresenceMatrix(
    private val width: Int,
    private val height: Int,
    private val inverted: Boolean = false,
    otherTrackables: Flow<List<Positionable>> = flowOf(emptyList())
) {

    companion object {
        private val scope = CoroutineScope(
            Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        )
    }

    private val _costs = Array(width) {
        Array(height) {
            if (inverted) {
                1
            } else {
                0
            }
        }
    }

    val costs: Array<Array<Int>>
        get() = _costs.map { row ->
            row.clone()
        }.toTypedArray()

    fun track(
        trackables: List<Positionable>
    ) {
        latestTrackables.value = latestTrackables.value.plus(trackables)
    }

    private val latestTrackables = MutableStateFlow<List<Positionable>>(listOf())
    private val combinedTrackables = latestTrackables.combine(otherTrackables) { a, b ->
        a.plus(b)
    }

    init {
        combinedTrackables.onEach { trackables ->
            trackables.forEach { trackable ->
                track(trackable)
            }
        }.launchIn(scope)

        otherTrackables.onEach { trackable ->
            println(trackable)
        }.launchIn(scope)
    }

    fun track(
        positionable: Positionable
    ) {
        positionable.position.runningFold<Position, Pair<Position?, Position>>(
            initial = Pair(null, positionable.position.value),
            operation = { acc, new ->
                Pair(acc.second, new)
            }
        ).onEach { (old, new) ->
            old?.also { (x, y) ->
                _costs[x][y] = if (inverted) {
                    1
                } else {
                    0
                }
            }
            new.also { (x, y) ->
                _costs[x][y] = if (inverted) {
                    0
                } else {
                    1
                }
            }
        }.launchIn(scope)
    }

    fun merge(other: EntityPresenceMatrix) = EntityPresenceMatrix(
        width = width,
        height = height,
        inverted = inverted,
        otherTrackables = this.latestTrackables.combine(other.latestTrackables) { a, b ->
            a.plus(b)
        }
    )
}