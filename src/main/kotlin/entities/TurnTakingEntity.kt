package entities

import actions.Action
import game.MapState
import kotlinx.serialization.Serializable

@Serializable
abstract class TurnTakingEntity : Entity() {
    abstract val speed: Int
    abstract suspend fun getAction(
        mapState: MapState
    ) : Action
}