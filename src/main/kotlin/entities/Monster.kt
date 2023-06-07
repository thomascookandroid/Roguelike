package entities

import state.actions.Action
import state.actions.ActionAttack
import state.actions.ActionMove
import state.actions.ActionNone
import algorithms.Djikstra
import algorithms.IMPASSABLE
import components.Positionable
import components.Queueable
import state.LocalMapState
import kotlinx.serialization.Serializable
import tiles.Tile
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

@Serializable
data class Monster(
    override var x: Int,
    override var y: Int,
    override val tile: Tile = Tile.MONSTER,
    override val drawPriority: Int = 1,
    override val speed: Int = 200
): Entity, Queueable {

    override fun getAction(
        localMapState: LocalMapState
    ): Action {
        return if (isAdjacent(this, localMapState.player)) {
            ActionAttack()
        } else {
            moveToPlayerAction(localMapState) ?: ActionNone()
        }
    }

    private fun isAdjacent(
        a: Positionable,
        b: Positionable
    ) : Boolean {
        val dx = abs(a.x - b.x)
        val dy = abs(a.y - b.y)
        return dx < 2 && dy < 2
    }

    private fun moveToPlayerAction(
        localMapState: LocalMapState
    ) : ActionMove? {
        val moveToPlayerCostGrid = Djikstra().floodFill(
            localMapState.columns,
            localMapState.rows,
            localMapState.playerGoalGrid,
            localMapState.obstacleCostGrid
        )

        val downhillNeighbours = cheapestNeighbours(
            x,
            y,
            moveToPlayerCostGrid, localMapState
        )

        return downhillNeighbours.minByOrNull { (x, y) ->
            cartesianDistance(
                localMapState.player.x,
                localMapState.player.y,
                x,
                y
            )
        }?.let { (moveToX, moveToY) ->
            ActionMove(
                moveToX - x,
                moveToY - y,
                this
            )
        }
    }

    private fun cartesianDistance(
        ax: Int,
        ay: Int,
        bx: Int,
        by: Int
    ) = sqrt(
        abs(ax.toDouble() - bx.toDouble()).pow(2.0) + abs(ay.toDouble() - by.toDouble()).pow(2.0)
    ).toInt()

    private fun cheapestNeighbours(
        x: Int,
        y: Int,
        costGrid: Array<Array<Int>>,
        localMapState: LocalMapState
    ) : List<Pair<Int, Int>> {
        var cheapest = IMPASSABLE
        val cheapestPositions = mutableListOf<Pair<Int, Int>>()
        (x - 1..x + 1).forEach { fx ->
            (y - 1..y + 1).forEach { fy ->
                if (fx in (0 until localMapState.columns) && fy in (0 until localMapState.rows)) {
                    val neighbourCost = costGrid[fx][fy]
                    if (neighbourCost < cheapest) {
                        cheapestPositions.clear()
                        cheapest = neighbourCost
                        cheapestPositions.add(fx to fy)
                    } else if (neighbourCost == cheapest && cheapest < IMPASSABLE) {
                        cheapestPositions.add(fx to fy)
                    }
                }
            }
        }
        return cheapestPositions
    }
}