package entities

import kotlinx.coroutines.flow.MutableStateFlow
import tiles.Tile

interface Renderable {
    val tile: Tile
    val position: MutableStateFlow<Position>
    val drawPriority: Int
}