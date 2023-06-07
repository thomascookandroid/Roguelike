package components

import tiles.Tile
import tiles.TileSet
import java.awt.Graphics

interface Renderable {
    val tile: Tile
    val drawPriority: Int

    fun render(
        graphics: Graphics,
        x: Int,
        y: Int,
        tileSet: TileSet,
        tileWidth: Int,
        tileHeight: Int
    ) {
        val tileDimensions = tileSet.getTileDimensions(tile)
        graphics.drawRect(
            x  * tileWidth,
            y * tileHeight,
            tileWidth,
            tileHeight
        )
        graphics.drawImage(
            tileSet.image,
            x * tileWidth,
            y * tileHeight,
            x * tileWidth + tileWidth,
            y * tileHeight + tileHeight,
            tileDimensions.left,
            tileDimensions.top,
            tileDimensions.right,
            tileDimensions.bottom,
            null
        )
    }
}