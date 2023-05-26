package entities

import kotlinx.coroutines.flow.MutableStateFlow
import tiles.Tile

data class Wall(
    override val position: MutableStateFlow<Position>,
    override val tile: Tile = Tile.WALL,
    override val drawPriority: Int = 0
): Entity(), Trackable