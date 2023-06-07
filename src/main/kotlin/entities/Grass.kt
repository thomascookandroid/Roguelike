package entities

import kotlinx.serialization.Serializable
import tiles.Tile

@Serializable
data class Grass(
    override var x: Int,
    override var y: Int,
    override val tile: Tile = Tile.GRASS,
    override val drawPriority: Int = 0
): Entity