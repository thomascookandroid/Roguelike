package entities

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable
import serialization.MutableStateFlowPositionSerializer
import tiles.Tile

@Serializable
data class Wall(
    @Serializable(with = MutableStateFlowPositionSerializer::class)
    override val position: MutableStateFlow<Position>,
    override val tile: Tile = Tile.WALL,
    override val drawPriority: Int = 0
): Entity(), Trackable