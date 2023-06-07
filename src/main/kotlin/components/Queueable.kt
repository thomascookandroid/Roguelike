package components

import state.actions.Action
import state.LocalMapState

interface Queueable {
    val speed: Int
    fun getAction(
        localMapState: LocalMapState
    ) : Action
}