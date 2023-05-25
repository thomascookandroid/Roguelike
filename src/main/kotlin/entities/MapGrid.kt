package entities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.util.TreeSet

class MapGrid(
    val width: Int,
    val height: Int
) {

    private val cells = Array(width) {
        Array(height) {
            TreeSet<Entity> { a, b ->
                b.drawPriority.compareTo(a.drawPriority)
            }
        }
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    fun untrackEntity(entity: Entity) {
        // TODO: Implement this
    }

    fun trackEntity(entity: Entity) {
        entity.position.runningFold<Position, Pair<Position?, Position?>>(
            initial = Pair(null, null),
            operation = { acc, new ->
                Pair(acc.second, new)
            }
        ).onEach { (old, new) ->
            old?.also { (x, y) ->
                cells[x][y].remove(entity)
            }
            new?.also { (x, y) ->
                cells[x][y].add(entity)
            }
        }.launchIn(scope)
    }
}