package actions

import state.MenuState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import state.LocalMapState

class ActionOpenMenu(
    private val mapState: LocalMapState
) : Action.Intermediate() {
    override fun run(
        scope: CoroutineScope
    ) = scope.launch {
        mapState.enterSubState(
            MenuState(
                mapState.columns,
                mapState.rows
            )
        )
    }
}