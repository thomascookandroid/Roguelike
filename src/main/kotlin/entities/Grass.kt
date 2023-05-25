package entities

import kotlinx.coroutines.flow.MutableStateFlow
import tiles.Tile

data class Grass(
    override val position: MutableStateFlow<Position>,
    override val tile: Tile = Tile.GRASS,
    override val drawPriority: Int = 0
): Entity()