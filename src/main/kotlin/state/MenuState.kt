package state

import data.Position
import entities.Entity
import entities.HealthBar
import input.CommandCode
import input.InputManager
import kotlinx.coroutines.flow.MutableStateFlow
import swing.Game.Renderer.render

class MenuState(
    override val rows: Int,
    override val columns: Int
) : State() {

    override val stateActivePredicate = {
        _isActive
    }

    private var _isActive = true

    override suspend fun onCreate() {

    }

    private tailrec fun getAction() : () -> Unit {
        return InputManager.consumeCurrentInput()?.let { commandCode ->
            when (commandCode) {
                CommandCode.COMMAND_CODE_OPEN_MENU -> {
                    {
                        _isActive = false
                    }
                }
                CommandCode.COMMAND_LEFT -> {
                    { }
                }
                CommandCode.COMMAND_UP -> {
                    { }
                }
                CommandCode.COMMAND_RIGHT -> {
                    { }
                }
                CommandCode.COMMAND_DOWN -> {
                    { }
                }
            }
        } ?: getAction()
    }

    override suspend fun onUpdate() {
        render()
        val action = getAction()
        action.invoke()
        render()
    }

    override val entities: List<Entity>
        get() = (0 until columns).map { column ->
            HealthBar(
                position = MutableStateFlow(
                    Position(
                        x = column,
                        y = 0
                    )
                )
            )
        }
}