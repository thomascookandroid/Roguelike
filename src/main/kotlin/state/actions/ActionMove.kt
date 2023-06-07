package state.actions

import components.Positionable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActionMove(
    private val dx: Int,
    private val dy: Int,
    private val positionable: Positionable
) : Action.Terminal() {
    override suspend fun run() {
        positionable.position.value = positionable.position.value.let { position ->
            position.copy(
                x = position.x + dx,
                y = position.y + dy
            )
        }
    }
}