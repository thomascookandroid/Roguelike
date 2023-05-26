package entities

import algorithms.Djikstra
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.util.TreeSet

class MapGrid(
    val width: Int,
    val height: Int
) {
    private val djikstra = Djikstra()

    fun getCostGrid(
        attractors: List<Position>,
        obstacles: List<Position>
    ) : Array<Array<Int>> {
        return djikstra.floodFill(
            width,
            height,
            attractors,
            obstacles
        )
    }

    val player: Player?
        get() = cells.flatMap { rows ->
            rows.flatMap { cell ->
                cell.map { entity ->
                    entity
                }
            }
        }.filterIsInstance<Player>().firstOrNull()

    val monsters: List<Monster>
        get() = cells.flatMap { rows ->
            rows.flatMap { cell ->
                cell.map { entity ->
                    entity
                }
            }
        }.filterIsInstance<Monster>()

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