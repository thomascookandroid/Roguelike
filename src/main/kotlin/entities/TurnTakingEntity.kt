package entities

import actions.Action
import game.MapState

abstract class TurnTakingEntity : Entity() {
    abstract val speed: Int
    abstract fun getAction(
        mapState: MapState
    ) : Action
}