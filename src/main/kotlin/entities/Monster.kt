package entities

import actions.Action
import actions.ActionMove
import kotlinx.coroutines.flow.MutableStateFlow
import tiles.Tile
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class Monster(
    override val position: MutableStateFlow<Position>,
    override val mapGrid: MapGrid,
    override val tile: Tile = Tile.MONSTER,
    override val drawPriority: Int = 1,
    override val speed: Int = 150
): TurnTakingEntity() {
    override fun getAction(): Action {
        val goalPosition = mapGrid.player?.position?.value
        val costGrid = mapGrid.getCostGrid(
            listOfNotNull(goalPosition),
            mapGrid.monsters.map { it.position.value }
        )
        val downhillNeighbours = cheapestNeighbours(position.value.x, position.value.y, costGrid)
        val moveTo = downhillNeighbours.sortedBy { (x, y) ->
            sqrt(
                abs((goalPosition?.x?.toDouble() ?: 0.0) - x.toDouble()).pow(2.0)
                + abs((goalPosition?.y?.toDouble() ?: 0.0) - y.toDouble()).pow(2.0)
            )
        }.first()
        return ActionMove(moveTo.x - position.value.x , moveTo.y - position.value.y, this, mapGrid)
    }

    private fun cheapestNeighbours(x: Int, y: Int, costGrid: Array<Array<Int>>) : List<Position> {
        var cheapest = Int.MAX_VALUE
        val cheapestPositions = mutableListOf<Position>()
        (x - 1..x + 1).forEach { fx ->
            (y - 1..y + 1).forEach { fy ->
                if (fx in (0 until mapGrid.width) && fy in (0 until mapGrid.height)) {
                    val neighbourCost = costGrid[fx][fy]
                    if (neighbourCost < cheapest) {
                        cheapestPositions.clear()
                        cheapest = neighbourCost
                        cheapestPositions.add(Position(fx, fy))
                    } else if (neighbourCost == cheapest) {
                        cheapestPositions.add(Position(fx, fy))
                    }
                }
            }
        }
        return cheapestPositions
    }


}