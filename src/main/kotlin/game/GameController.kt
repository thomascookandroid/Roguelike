package game

import algorithms.EntityPresenceMatrix
import entities.*
import serialization.LocalMapLoader
import serialization.LocalMapSaver
import state.LocalMapState
import tiles.TileSet
import java.awt.Color
import java.awt.Graphics

sealed class State {

    abstract val columns: Int
    abstract val rows: Int

    protected fun start() {
        onCreate()
        while (true) {
            onUpdate()
        }
    }

    abstract fun onCreate()

    abstract fun onUpdate()

    abstract val renderables: List<Renderable>

    abstract val subState: State?
}

class GameController {

    private val tileSet = TileSet(
        tilesetPath = "../tileset.png"
    )

    private val localMapLoader = LocalMapLoader()
    private val localMapSaver = LocalMapSaver()

    private lateinit var localMapState: LocalMapState

    fun start() {
        localMapState = LocalMapState(20, 20)
        localMapState.start()
    }

    fun render(
        graphics: Graphics,
        renderWidth: Int,
        renderHeight: Int
    ) {
        val tileWidth = renderWidth / localMapState.columns
        val tileHeight = renderHeight / localMapState.rows
        localMapState.renderables.forEach { renderable ->
            render(graphics, renderable, tileWidth, tileHeight)
        }
        localMapSaver.save(localMapState)
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