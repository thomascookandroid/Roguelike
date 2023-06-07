import swing.Game

fun main(
    args: Array<String>
) {
    GlobalVariables.shouldDebug = args.firstOrNull()?.let {
        it.toBoolean()
    } ?: false
    val game = Game()
}

object GlobalVariables {
    var shouldDebug = false
}