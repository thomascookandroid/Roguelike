package input

import java.awt.AWTEvent
import java.awt.Toolkit
import java.awt.event.AWTEventListener
import java.awt.event.KeyEvent
import java.util.concurrent.ConcurrentSkipListSet

object InputManager {
    private val inputSet = ConcurrentSkipListSet<InputCode>()

    private val eventListener = AWTEventListener { event ->
        if (event is KeyEvent) {
            event.toInputCode()?.also { inputCode ->
                if (event.id == KeyEvent.KEY_PRESSED) {
                    inputSet.add(inputCode)
                } else if (event.id == KeyEvent.KEY_RELEASED) {
                    inputSet.remove(inputCode)
                }
            }
        }
    }

    private fun KeyEvent.toInputCode() = when (this.keyCode) {
        KeyEvent.VK_LEFT -> InputCode.LEFT_ARROW
        KeyEvent.VK_UP -> InputCode.UP_ARROW
        KeyEvent.VK_RIGHT -> InputCode.RIGHT_ARROW
        KeyEvent.VK_DOWN -> InputCode.DOWN_ARROW
        else -> null
    }

    init {
        Toolkit
            .getDefaultToolkit()
            .addAWTEventListener(
                eventListener,
                AWTEvent.KEY_EVENT_MASK
            )
    }

    fun currentCommand() = if (inputSet.contains(InputCode.LEFT_ARROW)) {
        CommandCode.COMMAND_LEFT
    } else if (inputSet.contains(InputCode.UP_ARROW)) {
        CommandCode.COMMAND_UP
    } else if (inputSet.contains(InputCode.RIGHT_ARROW)) {
        CommandCode.COMMAND_RIGHT
    } else if (inputSet.contains(InputCode.DOWN_ARROW)) {
        CommandCode.COMMAND_DOWN
    } else {
        null
    }
}