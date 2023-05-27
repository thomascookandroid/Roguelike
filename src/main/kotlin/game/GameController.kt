package game

import algorithms.EntityPresenceMatrix
import entities.*
import kotlinx.coroutines.*
import tiles.TileSet
import java.awt.Color
import java.awt.Graphics

class GameController {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val tileSet = TileSet(
        tilesetPath = "../tileset.png"
    )

    private val mapState = MapState(
        columns = 20,
        rows = 20
    )

    private val turnQueue = TurnQueue().apply {
        add(mapState.player)
        add(mapState.monsters)
    }

    fun start(
        render: () -> Unit
    ) {
        scope.launch {
            while (turnQueue.isNotEmpty()) {
                render()
                val dequeued = turnQueue.poll()
                dequeued.getAction(mapState).run()
                turnQueue.add(dequeued)
            }
        }
    }

    fun render(
        graphics: Graphics,
        renderWidth: Int,
        renderHeight: Int
    ) {
        val tileWidth = renderWidth / mapState.columns
        val tileHeight = renderHeight / mapState.rows
        mapState.renderables.forEach { renderable ->
            render(graphics, renderable, tileWidth, tileHeight)
        }

        visualiseEntityPresenceMatrix(
            mapState.obstacleEntityPresenceMatrix,
            graphics,
            tileWidth,
            tileHeight
        )
    }

    private fun visualiseEntityPresenceMatrix(
        entityPresenceMatrix: EntityPresenceMatrix,
        graphics: Graphics,
        tileWidth: Int,
        tileHeight: Int
    ) {
        graphics.color = Color.RED
        entityPresenceMatrix.costs.forEachIndexed { x, rows ->
            rows.forEachIndexed { y, cell ->
                graphics.drawString(
                    cell.toString(),
                    x * tileWidth + tileWidth / 2,
                    y * tileHeight + tileHeight / 2
                )
            }
        }
    }

    private fun render(
        graphics: Graphics,
        renderable: Renderable,
        tileWidth: Int,
        tileHeight: Int
    ) {
        val tileDimensions = tileSet.getTileDimensions(renderable.tile)
        graphics.drawRect(
            renderable.position.value.x  * tileWidth,
            renderable.position.value.y * tileHeight,
            tileWidth,
            tileHeight
        )
        graphics.drawImage(
            tileSet.image,
            renderable.position.value.x * tileWidth,
            renderable.position.value.y * tileHeight,
            renderable.position.value.x * tileWidth + tileWidth,
            renderable.position.value.y * tileHeight + tileHeight,
            tileDimensions.left,
            tileDimensions.top,
            tileDimensions.right,
            tileDimensions.bottom,
            null
        )
    }
}