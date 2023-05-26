package entities

import actions.Action
import actions.ActionMove
import game.MapState
import input.CommandCode
import input.InputManager
import kotlinx.coroutines.flow.MutableStateFlow
import tiles.Tile

data class Player(
    override val position: MutableStateFlow<Position>,
    override val tile: Tile = Tile.PLAYER,
    override val drawPriority: Int = 1,
    override val speed: Int = 100
): TurnTakingEntity(), Trackable {
    override tailrec fun getAction(
        mapState: MapState
    ) : Action {
        return InputManager.consumeCurrentInput()?.let { commandCode ->
            when (commandCode) {
                CommandCode.COMMAND_LEFT -> ActionMove(-1, 0, this)
                CommandCode.COMMAND_UP -> ActionMove(0, -1, this)
                CommandCode.COMMAND_RIGHT -> ActionMove(1, 0, this)
                CommandCode.COMMAND_DOWN -> ActionMove(0, 1, this)
            }
        } ?: getAction(mapState)
    }
}