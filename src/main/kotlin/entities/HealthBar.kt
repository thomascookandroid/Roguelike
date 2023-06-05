package entities

import data.Position
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable
import serialization.MutableStateFlowPositionSerializer
import tiles.Tile

@Serializable
data class HealthBar(
    @Serializable(with = MutableStateFlowPositionSerializer::class)
    override val position: MutableStateFlow<Position>,
    override val tile: Tile = Tile.HEALTH_BAR,
    override val drawPriority: Int = 0
): Entity