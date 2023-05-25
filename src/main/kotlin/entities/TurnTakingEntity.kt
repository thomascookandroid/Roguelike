package entities

import actions.Action

abstract class TurnTakingEntity : Entity() {
    abstract val speed: Int
    abstract val mapGrid: MapGrid
    abstract fun getAction() : Action
}