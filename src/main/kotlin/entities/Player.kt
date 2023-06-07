package entities

import state.actions.Action
import state.actions.ActionMove
import state.actions.ActionOpenMenu
import algorithms.EntityPresenceMatrix
import components.Queueable
import data.Position
import state.LocalMapState
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
): Entity, Queueable {
    override tailrec fun getAction(
        localMapState: LocalMapState
    ) : Action {
        return InputManager.consumeCurrentInput()?.let { commandCode ->
            when (commandCode) {
                CommandCode.COMMAND_CODE_OPEN_MENU -> {
                    ActionOpenMenu(
                        localMapState
                    )
                }
                CommandCode.COMMAND_LEFT -> moveActionIfValid(
                    localMapState.obstacleEntityPresenceMatrix,
                    -1,
                    0
                )
                CommandCode.COMMAND_UP -> moveActionIfValid(
                    localMapState.obstacleEntityPresenceMatrix,
                    0,
                    -1
                )
                CommandCode.COMMAND_RIGHT -> moveActionIfValid(
                    localMapState.obstacleEntityPresenceMatrix,
                    1,
                    0
                )
                CommandCode.COMMAND_DOWN -> moveActionIfValid(
                    localMapState.obstacleEntityPresenceMatrix,
                    0,
                    1
                )
            }
        } ?: getAction(localMapState)
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