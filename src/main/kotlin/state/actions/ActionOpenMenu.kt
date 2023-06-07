package state.actions

import state.MenuState
import state.LocalMapState

class ActionOpenMenu(
    private val mapState: LocalMapState
) : Action.Intermediate() {
    override suspend fun run() {
        mapState.enterSubState(
            MenuState(
                mapState.columns,
                mapState.rows
            )
        )
    }
}