package rendering

import entities.Entity
import tiles.TileSet
import java.awt.Graphics

class MapRenderer {

    private val tileRenderer = TileRenderer(
        TileSet(
            tilesetPath = "../tileset.png"
        )
    )

    fun renderEntities(
        entities: List<Entity>,
        renderWidth: Int,
        renderHeight: Int,
        graphics: Graphics
    ) {
        val tileWidth = renderWidth / 20
        val tileHeight = renderHeight / 20
        entities.forEach { entity ->
            tileRenderer.draw(
                graphics,
                entity.tile,
                entity.position.value.x * tileWidth,
                entity.position.value.y * tileHeight,
                entity.position.value.x * tileWidth + tileWidth,
                entity.position.value.y * tileHeight + tileHeight
            )
        }
    }
}