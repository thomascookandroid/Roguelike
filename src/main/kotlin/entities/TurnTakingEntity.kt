package entities

import actions.Action
import state.LocalMapState
import kotlinx.serialization.Serializable

@Serializable
abstract class TurnTakingEntity : Entity() {
    abstract val speed: Int
    abstract fun getAction(
        localMapState: LocalMapState
    ) : Action
}