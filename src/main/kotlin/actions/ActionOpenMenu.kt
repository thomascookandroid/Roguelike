package actions

import game.MenuState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ActionOpenMenu(
    private val menuColumns: Int,
    private val menuRows: Int
) : Action() {
    override fun run(
        scope: CoroutineScope
    ) = scope.launch {
        MenuState(
            menuColumns,
            menuRows
        ).start()
    }
}