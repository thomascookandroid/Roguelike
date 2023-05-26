package algorithms

import entities.Position
import entities.Trackable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class EntityPresenceMatrix(
    width: Int,
    height: Int,
    private val inverted: Boolean = false
) {

    private val scope = CoroutineScope(Dispatchers.IO)

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
        trackables: List<Trackable>
    ) {
        trackables.forEach { trackable ->
            track(trackable)
        }
    }

    fun track(
        trackable: Trackable
    ) {
        trackable.position.runningFold<Position, Pair<Position?, Position>>(
            initial = Pair(null, trackable.position.value),
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
}