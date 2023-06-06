package state

import algorithms.EntityPresenceMatrix
import data.Position
import entities.*
import game.TurnQueue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable
import swing.Game.Renderer.render

@Serializable
class LocalMapState(
    override val columns: Int,
    override val rows: Int
) : State() {

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

    private val monsters = listOf(
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

    override val entities: List<Entity>
        get() = listOf(player).plus(monsters).plus(walls).plus(grass)

    @kotlinx.serialization.Transient
    override val stateActivePredicate = {
        turnQueue.isNotEmpty()
    }

    override fun onCreate() {
        playerEntityPresenceMatrix.track(player)
        monsterEntityPresenceMatrix.track(monsters)
        wallEntityPresenceMatrix.track(walls)
        turnQueue.add(player)
        turnQueue.add(monsters)
    }

    override suspend fun onUpdate() {
        render()
        turnQueue.takeTurn(scope, this)
    }
}