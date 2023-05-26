package actions

import entities.Entity

abstract class Action(
    protected val entity: Entity
) {
    abstract fun run()
}