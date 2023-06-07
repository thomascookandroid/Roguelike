package state

import entities.*
import game.TurnQueue
import kotlinx.serialization.Serializable
import java.awt.Color
import java.awt.Graphics

@Serializable
class LocalMapState(
    override val columns: Int,
    override val rows: Int,
    override val entities: List<Entity>
) : State() {

    private val turnQueue = TurnQueue()

    val player: Player
        get() = entities.filterIsInstance<Player>().first()

    private val monsters: List<Monster>
        get() = entities.filterIsInstance<Monster>()

    private val walls: List<Wall>
        get() = entities.filterIsInstance<Wall>()

    val obstacleCostGrid: Array<Array<Int>>
        get() {
            val wallCostsMap = walls.associate { wall ->
                (wall.x to wall.y) to 1
            }
            val monsterCostsMap = monsters.associate { monster ->
                (monster.x to monster.y) to 1
            }
            return Array(columns) { x ->
                Array(rows) { y ->
                    wallCostsMap[x to y]
                        ?: monsterCostsMap[x to y]
                        ?: 0
                }
            }
        }

    val playerGoalGrid: Array<Array<Int>>
        get() {
            return Array(columns) { x ->
                Array(rows) { y ->
                    if (player.x == x && player.y == y) {
                        0
                    } else {
                        1
                    }
                }
            }
        }

    @kotlinx.serialization.Transient
    override val stateActivePredicate = {
        turnQueue.isNotEmpty()
    }

    override suspend fun onCreate() {
        turnQueue.add(player)
        turnQueue.add(monsters)
    }

    override suspend fun onUpdate() {
        turnQueue.takeTurn(
            localMapState = this
        )
    }

    override fun renderDebug(
        graphics: Graphics,
        tileWidth: Int,
        tileHeight: Int
    ) {
        val oldColor = graphics.color
        graphics.color = Color.BLUE
        obstacleCostGrid.forEachIndexed { x, rows ->
            rows.forEachIndexed { y, cell ->
                graphics.drawString(
                    cell.coerceAtMost(100).toString(),
                    x * tileWidth + tileWidth / 2,
                    y * tileHeight + tileHeight / 2
                )
            }
        }
        graphics.color = oldColor
    }
}