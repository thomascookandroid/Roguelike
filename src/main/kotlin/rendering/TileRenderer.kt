package rendering

import tiles.Tile
import tiles.TileSet
import java.awt.Graphics

class TileRenderer(
    private val tileSet: TileSet
) {
    fun draw(graphics: Graphics, tile: Tile, left: Int, top: Int, right: Int, bottom: Int) {
        val tileDimensions = tileSet.getTileDimensions(tile)
        graphics.drawImage(
            tileSet.image,
            left,
            top,
            right,
            bottom,
            tileDimensions.left,
            tileDimensions.top,
            tileDimensions.right,
            tileDimensions.bottom,
            null
        )
    }
}