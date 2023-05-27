package input

import java.awt.AWTEvent
import java.awt.Toolkit
import java.awt.event.AWTEventListener
import java.awt.event.KeyEvent
import java.util.*
import java.util.concurrent.ConcurrentSkipListSet
import java.util.concurrent.CopyOnWriteArraySet

object InputManager {
    private val inputSet = Collections.synchronizedSet<InputCode>(mutableSetOf())

    private val eventListener = AWTEventListener { event ->
        if (event is KeyEvent) {
            event.toInputCode()?.also { inputCode ->
                if (event.id == KeyEvent.KEY_PRESSED) {
                    inputSet.add(inputCode)
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

    fun consumeCurrentInput() : CommandCode? {
        val mappedCommand = if (inputSet.contains(InputCode.LEFT_ARROW)) {
            inputSet.clear()
            CommandCode.COMMAND_LEFT
        } else if (inputSet.contains(InputCode.UP_ARROW)) {
            inputSet.clear()
            CommandCode.COMMAND_UP
        } else if (inputSet.contains(InputCode.RIGHT_ARROW)) {
            inputSet.clear()
            CommandCode.COMMAND_RIGHT
        } else if (inputSet.contains(InputCode.DOWN_ARROW)) {
            inputSet.clear()
            CommandCode.COMMAND_DOWN
        } else {
            null
        }
        return mappedCommand
    }
}