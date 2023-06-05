package components

import actions.Action
import state.LocalMapState

interface Queueable {
    val speed: Int
    fun getAction(
        localMapState: LocalMapState
    ) : Action
}