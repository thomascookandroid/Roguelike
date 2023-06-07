package game

import GlobalVariables
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import serialization.LocalMapLoader
import serialization.LocalMapSaver
import state.LocalMapState
import tiles.TileSet
import java.awt.Graphics
import java.util.concurrent.Executors

class GameController {

    private val tileSet = TileSet(
        tilesetPath = "../tileset.png"
    )

    private val localMapLoader = LocalMapLoader()
    private val localMapSaver = LocalMapSaver()

    private lateinit var localMapState: LocalMapState

    private val scope = CoroutineScope(
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    )

    fun start() {
        localMapState = localMapLoader.load()
        scope.launch {
            localMapState.start()
        }
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
        if (GlobalVariables.shouldDebug)
            localMapState.renderDebug(
                graphics,
                tileWidth,
                tileHeight
            )
        localMapSaver.save(localMapState)
    }
}