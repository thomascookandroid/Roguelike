package components

import data.Position
import tiles.Tile
import tiles.TileSet
import java.awt.Graphics

interface Renderable {
    val tile: Tile
    val drawPriority: Int

    fun render(
        graphics: Graphics,
        position: Position,
        tileSet: TileSet,
        tileWidth: Int,
        tileHeight: Int
    ) {
        val tileDimensions = tileSet.getTileDimensions(tile)
        graphics.drawRect(
            position.x  * tileWidth,
            position.y * tileHeight,
            tileWidth,
            tileHeight
        )
        graphics.drawImage(
            tileSet.image,
            position.x * tileWidth,
            position.y * tileHeight,
            position.x * tileWidth + tileWidth,
            position.y * tileHeight + tileHeight,
            tileDimensions.left,
            tileDimensions.top,
            tileDimensions.right,
            tileDimensions.bottom,
            null
        )
    }
}