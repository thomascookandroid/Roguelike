package state

import entities.Renderable
import input.CommandCode
import input.InputManager
import swing.Game

class MenuState(
    private val rows: Int,
    private val columns: Int
) {

    private val renderables = emptyList<Renderable>()

    fun start() {
        while (true) {
            when (InputManager.consumeCurrentInput()) {
                CommandCode.COMMAND_CODE_OPEN_MENU -> {
                    break
                }

                else -> {
                    // Perform menu action
                    Game.render()
                }
            }
        }
    }
}