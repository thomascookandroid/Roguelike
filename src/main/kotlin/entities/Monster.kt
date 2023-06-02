package entities

import actions.Action
import actions.ActionAttack
import actions.ActionMove
import actions.ActionNone
import algorithms.Djikstra
import algorithms.IMPASSABLE
import state.LocalMapState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable
import serialization.MutableStateFlowPositionSerializer
import tiles.Tile
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

@Serializable
data class Monster(
    @Serializable(with = MutableStateFlowPositionSerializer::class)
    override val position: MutableStateFlow<Position>,
    override val tile: Tile = Tile.MONSTER,
    override val drawPriority: Int = 1,
    override val speed: Int = 200
): TurnTakingEntity(), Trackable {
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
        a: Entity,
        b: Entity
    ) : Boolean {
        val (ax, ay) = a.position.value
        val (bx, by) = b.position.value
        val dx = abs(ax - bx)
        val dy = abs(ay - by)
        return dx < 2 && dy < 2
    }

    private fun moveToPlayerAction(
        localMapState: LocalMapState
    ) : ActionMove? {
        val moveToPlayerCostGrid = Djikstra().floodFill(
            localMapState.columns,
            localMapState.rows,
            localMapState.playerEntityPresenceMatrix,
            localMapState.obstacleEntityPresenceMatrix
        )

        val downhillNeighbours = cheapestNeighbours(
            position.value.x,
            position.value.y,
            moveToPlayerCostGrid, localMapState
        )

        return downhillNeighbours.sortedBy { (x, y) ->
            cartesianDistance(
                localMapState.player.position.value.x,
                localMapState.player.position.value.y,
                x,
                y
            )
        }.firstOrNull()?.let { moveTo ->
            return ActionMove(
                moveTo.x - position.value.x,
                moveTo.y - position.value.y,
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
    ) : List<Position> {
        var cheapest = IMPASSABLE
        val cheapestPositions = mutableListOf<Position>()
        (x - 1..x + 1).forEach { fx ->
            (y - 1..y + 1).forEach { fy ->
                if (fx in (0 until localMapState.columns) && fy in (0 until localMapState.rows)) {
                    val neighbourCost = costGrid[fx][fy]
                    if (neighbourCost < cheapest) {
                        cheapestPositions.clear()
                        cheapest = neighbourCost
                        cheapestPositions.add(Position(fx, fy))
                    } else if (neighbourCost == cheapest && cheapest < IMPASSABLE) {
                        cheapestPositions.add(Position(fx, fy))
                    }
                }
            }
        }
        return cheapestPositions
    }
}