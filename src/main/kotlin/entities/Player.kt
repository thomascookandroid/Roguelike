package entities

import actions.Action
import actions.ActionMove
import algorithms.EntityPresenceMatrix
import game.MapState
import input.CommandCode
import input.InputManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable
import serialization.MutableStateFlowPositionSerializer
import tiles.Tile

@Serializable
data class Player(
    @Serializable(with = MutableStateFlowPositionSerializer::class)
    override val position: MutableStateFlow<Position>,
    override val tile: Tile = Tile.PLAYER,
    override val drawPriority: Int = 1,
    override val speed: Int = 100
): TurnTakingEntity(), Trackable {
    override tailrec suspend fun getAction(
        mapState: MapState
    ) : Action {
        return InputManager.consumeCurrentInput()?.let { commandCode ->
            when (commandCode) {
                CommandCode.COMMAND_LEFT -> moveActionIfValid(
                    mapState.obstacleEntityPresenceMatrix,
                    -1,
                    0
                )
                CommandCode.COMMAND_UP -> moveActionIfValid(
                    mapState.obstacleEntityPresenceMatrix,
                    0,
                    -1
                )
                CommandCode.COMMAND_RIGHT -> moveActionIfValid(
                    mapState.obstacleEntityPresenceMatrix,
                    1,
                    0
                )
                CommandCode.COMMAND_DOWN -> moveActionIfValid(
                    mapState.obstacleEntityPresenceMatrix,
                    0,
                    1
                )
            }
        } ?: getAction(mapState)
    }

    private fun moveActionIfValid(
        obstacleEntityPresenceMatrix: EntityPresenceMatrix,
        dx: Int,
        dy: Int
    ) : ActionMove? = if (isMoveValid(obstacleEntityPresenceMatrix, dx, dy)) {
        ActionMove(dx, dy, this)
    } else {
        null
    }

    private fun isMoveValid(
        obstacleEntityPresenceMatrix: EntityPresenceMatrix,
        dx: Int,
        dy: Int
    ) : Boolean {
        val newX = position.value.x + dx
        val newY = position.value.y + dy
        return obstacleEntityPresenceMatrix.costs[newX][newY] == 0
    }
}