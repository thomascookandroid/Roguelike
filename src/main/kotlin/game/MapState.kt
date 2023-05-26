package game

import algorithms.EntityPresenceMatrix
import entities.*
import kotlinx.coroutines.flow.MutableStateFlow

class MapState(
    val columns: Int,
    val rows: Int
) {
    val playerEntityPresenceMatrix = EntityPresenceMatrix(
        inverted = true,
        width = columns,
        height = rows
    )

    val wallEntityPresenceMatrix = EntityPresenceMatrix(
        width = columns,
        height = rows,
    )

    val monsterEntityPresenceMatrix = EntityPresenceMatrix(
        width = columns,
        height = rows
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

    val monster = Monster(
        position = MutableStateFlow(
            Position(
                x = 10,
                y = 10
            )
        )
    )

    val renderables: List<Renderable>
        get() = listOf(player).plus(monster).plus(walls).plus(grass)

    init {
        playerEntityPresenceMatrix.track(player)
        monsterEntityPresenceMatrix.track(monster)
        wallEntityPresenceMatrix.track(walls)
    }
}