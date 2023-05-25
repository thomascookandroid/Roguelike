package actions

import entities.Entity
import entities.MapGrid
import sun.rmi.runtime.Log
import java.util.logging.Level
import java.util.logging.Logger

class ActionMove(
    private val dx: Int,
    private val dy: Int,
    entity: Entity,
    mapGrid: MapGrid,
) : Action(
    entity,
    mapGrid
) {
    override fun run() {
        Logger.getLogger("ActionMove").log(Level.INFO, "Moving $entity by x: $dx, y: $dy")
        Logger.getLogger("ActionMove").log(Level.INFO, "Before: ${entity.position.value}")
        entity.position.value = entity.position.value.let { position ->
            position.copy(
                x = position.x + dx,
                y = position.y + dy
            )
        }
        Logger.getLogger("ActionMove").log(Level.INFO, "After: ${entity.position.value}")
    }
}