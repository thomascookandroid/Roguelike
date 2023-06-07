package state.actions

sealed class Action {
    open suspend fun run() {
        // Do nothing
    }

    abstract class Terminal : Action()
    abstract class Intermediate : Action()
}