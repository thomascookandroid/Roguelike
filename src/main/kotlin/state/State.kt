package state

import entities.Entity
import tiles.TileSet
import java.awt.Graphics
import swing.Game.Renderer.render

sealed class State {

    abstract val columns: Int
    abstract val rows: Int

    suspend fun start(
        onStateDeactivate: (suspend () -> Unit)? = null
    ) {
        onCreate()
        while (stateActivePredicate()) {
            render()
            onUpdate()
        }
        onStateDeactivate?.invoke()
    }

    fun render(
        graphics: Graphics,
        tileSet: TileSet,
        tileWidth: Int,
        tileHeight: Int
    ) {
        render(
            this,
            graphics,
            tileSet,
            tileWidth,
            tileHeight
        )
    }

    open fun renderDebug(
        graphics: Graphics,
        tileWidth: Int,
        tileHeight: Int
    ) {
        // Do nothing
    }

    private fun render(
        state: State?,
        graphics: Graphics,
        tileSet: TileSet,
        tileWidth: Int,
        tileHeight: Int
    ) {
        state?.apply {
            entities.forEach { entity ->
                entity.render(
                    graphics,
                    entity.x,
                    entity.y,
                    tileSet,
                    tileWidth,
                    tileHeight
                )
            }
            render(
                subState,
                graphics,
                tileSet,
                tileWidth,
                tileHeight
            )
        } ?: return
    }

    abstract val stateActivePredicate: () -> Boolean

    abstract suspend fun onCreate()

    abstract suspend fun onUpdate()

    abstract val entities: List<Entity>

    suspend fun enterSubState(
        state: State
    ) {
        subState = state
        state.start {
            subState = null
        }
    }

    private var subState: State? = null
}