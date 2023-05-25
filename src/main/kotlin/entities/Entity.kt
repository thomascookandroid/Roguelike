package entities

import kotlinx.coroutines.flow.MutableStateFlow
import tiles.Tile

sealed class Entity {
    abstract val tile: Tile
    abstract val position: MutableStateFlow<Position>
    abstract val drawPriority: Int
}