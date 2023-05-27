package actions

import entities.Entity

class ActionMove(
    private val dx: Int,
    private val dy: Int,
    private val entity: Entity
) : Action() {
    override fun run() {
        entity.position.value = entity.position.value.let { position ->
            position.copy(
                x = position.x + dx,
                y = position.y + dy
            )
        }
    }
}