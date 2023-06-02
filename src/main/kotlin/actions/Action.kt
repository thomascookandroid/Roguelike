package actions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class Action {
    open fun run(
        scope: CoroutineScope
    ): Job = scope.launch {
        // Do nothing
    }
}