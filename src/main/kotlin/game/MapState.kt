package game

import algorithms.EntityPresenceMatrix
import entities.*
import input.CommandCode
import input.InputManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable
import swing.frames.Game.Renderer.render
import java.util.concurrent.Executors

@Serializable
class MapState(
    val columns: Int,
    val rows: Int
) {

    @kotlinx.serialization.Transient
    private val scope = CoroutineScope(
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    )

    private val turnQueue = TurnQueue()

    @kotlinx.serialization.Transient
    val playerEntityPresenceMatrix = EntityPresenceMatrix(
        width = columns,
        height = rows,
        inverted = true
    )

    @kotlinx.serialization.Transient
    val wallEntityPresenceMatrix = EntityPresenceMatrix(
        width = columns,
        height = rows,
    )

    @kotlinx.serialization.Transient
    val monsterEntityPresenceMatrix = EntityPresenceMatrix(
        width = columns,
        height = rows
    )

    @kotlinx.serialization.Transient
    val obstacleEntityPresenceMatrix = wallEntityPresenceMatrix.merge(
        monsterEntityPresenceMatrix
    )

    private val grass = (0 until columns).flatMap { x ->
        (0 until rows).map { y ->
            Grass(
                position = MutableStateFlow(
                    Position(
                        x = x,
                        y = y
                    )
                )
            )
        }
    }

    private val walls = listOf(
        Wall(
            position = MutableStateFlow(
                Position(
                    x = 9,
                    y = 9
                )
            )
        ),
        Wall(
            position = MutableStateFlow(
                Position(
                    x = 9,
                    y = 10
                )
            )
        ),
        Wall(
            position = MutableStateFlow(
                Position(
                    x = 9,
                    y = 11
                )
            )
        ),
        Wall(
            position = MutableStateFlow(
                Position(
                    x = 9,
                    y = 12
                )
            )
        ),
        Wall(
            position = MutableStateFlow(
                Position(
                    x = 10,
                    y = 9
                )
            )
        ),
        Wall(
            position = MutableStateFlow(
                Position(
                    x = 11,
                    y = 9
                )
            )
        ),
        Wall(
            position = MutableStateFlow(
                Position(
                    x = 11,
                    y = 10
                )
            )
        ),
        Wall(
            position = MutableStateFlow(
                Position(
                    x = 11,
                    y = 11
                )
            )
        ),
        Wall(
            position = MutableStateFlow(
                Position(
                    x = 11,
                    y = 12
                )
            )
        )
    )

    val player = Player(
        position = MutableStateFlow(
            Position(
                x = 10,
                y = 3
            )
        )
    )

    val monsters = listOf(
        Monster(
            position = MutableStateFlow(
                Position(
                    x = 10,
                    y = 10
                )
            )
        ),
        Monster(
            position = MutableStateFlow(
                Position(
                    x = 10,
                    y = 11
                )
            )
        ),
        Monster(
            position = MutableStateFlow(
                Position(
                    x = 10,
                    y = 12
                )
            )
        )
    )

    val renderables: List<Renderable>
        get() = listOf(player).plus(monsters).plus(walls).plus(grass)

    fun start() {
        playerEntityPresenceMatrix.track(player)
        monsterEntityPresenceMatrix.track(monsters)
        wallEntityPresenceMatrix.track(walls)
        turnQueue.add(player)
        turnQueue.add(monsters)
        scope.launch {
            while (turnQueue.isNotEmpty()) {
                render()
                val dequeued = turnQueue.poll()
                dequeued.getAction(
                    mapState = this@MapState
                ).run(
                    scope = scope
                ).join()
                turnQueue.add(dequeued)
            }
        }
    }
}

class MenuState(
    private val rows: Int,
    private val columns: Int
) {

    private val renderables = emptyList<Renderable>()

    fun start() {
        while (true) {
            when (InputManager.consumeCurrentInput()) {
                CommandCode.COMMAND_CODE_OPEN_MENU -> {
                    break
                }
                else -> {
                    // Perform menu action
                    render()
                }
            }
        }
    }
}

class GameStateMachine {

}