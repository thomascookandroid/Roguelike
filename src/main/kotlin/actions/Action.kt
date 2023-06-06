package actions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

sealed class Action {
    open fun run(
        scope: CoroutineScope
    ) : Job = scope.launch {
        // Do nothing
    }

    abstract class Terminal : Action()
    abstract class Intermediate : Action()
}