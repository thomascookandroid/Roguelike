package state

import entities.Entity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import tiles.TileSet
import java.awt.Graphics
import java.util.concurrent.Executors

sealed class State {

    abstract val columns: Int
    abstract val rows: Int

    companion object {
        @JvmStatic
        protected val scope = CoroutineScope(
            Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        )
    }

    fun start(
        onStateDeactivate: (() -> Unit)? = null
    ) {
        onCreate()
        scope.launch {
            while (stateActivePredicate()) {
                onUpdate()
            }
            onStateDeactivate?.invoke()
        }
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
                    entity.position.value,
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

    abstract fun onCreate()

    abstract suspend fun onUpdate()

    abstract val entities: List<Entity>

    fun enterSubState(
        state: State
    ) {
        subState = state
        state.start {
            subState = null
        }
    }

    var subState: State? = null
        private set
}