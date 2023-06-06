package actions

import components.Positionable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ActionMove(
    private val dx: Int,
    private val dy: Int,
    private val positionable: Positionable
) : Action.Terminal() {
    override fun run(
        scope: CoroutineScope
    ) = scope.launch {
        positionable.position.value = positionable.position.value.let { position ->
            position.copy(
                x = position.x + dx,
                y = position.y + dy
            )
        }
    }
}