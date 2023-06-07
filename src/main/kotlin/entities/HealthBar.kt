package entities

import kotlinx.serialization.Serializable
import tiles.Tile

@Serializable
data class HealthBar(
    override var x: Int,
    override var y: Int,
    override val tile: Tile = Tile.HEALTH_BAR,
    override val drawPriority: Int = 0
): Entity