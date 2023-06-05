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

    private var currentCommandCode: CommandCode? = null

    override val stateActivePredicate = {
        currentCommandCode = InputManager.consumeCurrentInput()
        when (currentCommandCode) {
            CommandCode.COMMAND_CODE_OPEN_MENU -> false
            else -> true
        }
    }

    override fun onCreate() {

    }

    override suspend fun onUpdate() {
        if (currentCommandCode == CommandCode.COMMAND_DOWN) {

        } else if (currentCommandCode == CommandCode.COMMAND_UP) {

        } else if (currentCommandCode == CommandCode.COMMAND_LEFT) {

        } else if (currentCommandCode == CommandCode.COMMAND_RIGHT) {

        }
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