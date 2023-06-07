package entities

import state.actions.Action
import state.actions.ActionMove
import state.actions.ActionOpenMenu
import components.Queueable
import state.LocalMapState
import input.CommandCode
import input.InputManager
import kotlinx.serialization.Serializable
import tiles.Tile

@Serializable
data class Player(
    override var x: Int,
    override var y: Int,
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
                    localMapState.obstacleCostGrid,
                    -1,
                    0
                )
                CommandCode.COMMAND_UP -> moveActionIfValid(
                    localMapState.obstacleCostGrid,
                    0,
                    -1
                )
                CommandCode.COMMAND_RIGHT -> moveActionIfValid(
                    localMapState.obstacleCostGrid,
                    1,
                    0
                )
                CommandCode.COMMAND_DOWN -> moveActionIfValid(
                    localMapState.obstacleCostGrid,
                    0,
                    1
                )
            }
        } ?: getAction(localMapState)
    }

    private fun moveActionIfValid(
        obstacleCostGrid: Array<Array<Int>>,
        dx: Int,
        dy: Int
    ) : ActionMove? = if (isMoveValid(obstacleCostGrid, dx, dy)) {
        ActionMove(dx, dy, this)
    } else {
        null
    }

    private fun isMoveValid(
        obstacleCostGrid: Array<Array<Int>>,
        dx: Int,
        dy: Int
    ) : Boolean {
        val newX = x + dx
        val newY = y + dy
        return obstacleCostGrid[newX][newY] == 0
    }
}