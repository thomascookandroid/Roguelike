package entities

import actions.Action
import actions.ActionMove
import kotlinx.coroutines.flow.MutableStateFlow
import tiles.Tile

data class Monster(
    override val position: MutableStateFlow<Position>,
    override val mapGrid: MapGrid,
    override val tile: Tile = Tile.PLAYER,
    override val drawPriority: Int = 1,
    override val speed: Int = 200
): TurnTakingEntity() {
    override fun getAction(): Action {
        return ActionMove(-1, 0, this, mapGrid)
    }
}