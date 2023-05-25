package actions

import entities.Entity
import entities.MapGrid

abstract class Action(
    protected val entity: Entity,
    protected val mapGrid: MapGrid
) {
    abstract fun run()
}