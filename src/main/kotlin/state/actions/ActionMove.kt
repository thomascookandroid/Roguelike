package state.actions

import components.Positionable

class ActionMove(
    private val dx: Int,
    private val dy: Int,
    private val positionable: Positionable
) : Action.Terminal() {
    override suspend fun run() {
        positionable.x += dx
        positionable.y += dy
    }
}