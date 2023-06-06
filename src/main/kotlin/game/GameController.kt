package game

import algorithms.EntityPresenceMatrix
import serialization.LocalMapLoader
import serialization.LocalMapSaver
import state.LocalMapState
import tiles.TileSet
import java.awt.Color
import java.awt.Graphics
import java.lang.Exception

class GameController {

    private val tileSet = TileSet(
        tilesetPath = "../tileset.png"
    )

    private val localMapLoader = LocalMapLoader()
    private val localMapSaver = LocalMapSaver()

    private lateinit var localMapState: LocalMapState

    fun start() {
        localMapState = localMapLoader.load()
        localMapState.start()
    }

    fun render(
        graphics: Graphics,
        renderWidth: Int,
        renderHeight: Int
    ) {
        val tileWidth = renderWidth / localMapState.columns
        val tileHeight = renderHeight / localMapState.rows
        localMapState.render(
            graphics,
            tileSet,
            tileWidth,
            tileHeight
        )
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
}