package game

import algorithms.EntityPresenceMatrix
import entities.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tiles.TileSet
import java.awt.Color
import java.awt.Graphics
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.lang.Exception

class MapLoader {
    fun loadMap() : MapState {
        return Json.decodeFromString(
            File("./src/main/resources/mapstate.json").readText(Charsets.UTF_8)
        )
    }
}

class MapSaver {
    fun saveMap(mapState: MapState) {
        try {
            PrintWriter(
                FileWriter(
                    "./src/main/resources/mapstate.json"
                )
            ).use { printWriter ->
                printWriter.write(
                    Json.encodeToString(mapState)
                )
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}

class GameController {

    private val tileSet = TileSet(
        tilesetPath = "../tileset.png"
    )

    private val mapLoader = MapLoader()
    private val mapSaver = MapSaver()

    private lateinit var mapState: MapState

    fun start() {
        mapState = MapState(20, 20)
        mapState.start()
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
        mapSaver.saveMap(mapState)
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