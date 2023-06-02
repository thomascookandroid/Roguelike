package actions

import entities.Entity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ActionMove(
    private val dx: Int,
    private val dy: Int,
    private val entity: Entity
) : Action() {
    override fun run(
        scope: CoroutineScope
    ) = scope.launch {
        entity.position.value = entity.position.value.let { position ->
            position.copy(
                x = position.x + dx,
                y = position.y + dy
            )
        }
    }
}