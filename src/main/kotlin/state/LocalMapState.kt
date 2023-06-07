package state

import algorithms.EntityPresenceMatrix
import entities.*
import game.TurnQueue
import kotlinx.serialization.Serializable
import swing.Game.Renderer.render
import java.awt.Color
import java.awt.Graphics

@Serializable
class LocalMapState(
    override val columns: Int,
    override val rows: Int,
    override val entities: List<Entity>
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

    val player: Player
        get() = entities.filterIsInstance<Player>().first()

    private val monsters: List<Monster>
        get() = entities.filterIsInstance<Monster>()

    private val walls: List<Wall>
        get() = entities.filterIsInstance<Wall>()

    @kotlinx.serialization.Transient
    override val stateActivePredicate = {
        turnQueue.isNotEmpty()
    }

    override suspend fun onCreate() {
        playerEntityPresenceMatrix.track(player)
        monsterEntityPresenceMatrix.track(monsters)
        wallEntityPresenceMatrix.track(walls)
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
        playerEntityPresenceMatrix.costs.forEachIndexed { x, rows ->
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