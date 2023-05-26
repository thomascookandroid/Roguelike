package tiles

import java.awt.Image
import java.awt.Toolkit
import java.awt.image.ImageObserver

class TileSet(
    tilesetPath: String,
    tilesetColumns: Int = 48,
    tilesetRows: Int = 22
) {
    val image: Image = Toolkit.getDefaultToolkit().getImage(
        javaClass.getResource(tilesetPath)
    )

    private var tileWidth = 0
    private var tileHeight = 0

    private val imageObserver = ImageObserver { _, _, _, _, width, height ->
        tileWidth = width / tilesetColumns
        tileHeight = height / tilesetRows
        true
    }

    init {
        image.getWidth(imageObserver)
        image.getHeight(imageObserver)
    }

    private data class TilePosition(
        val column: Int,
        val row: Int
    )

    data class TileDimensions(
        val left: Int,
        val top: Int,
        val right: Int,
        val bottom: Int
    )

    private val tilePositionMap = mapOf(
        Tile.NONE to TilePosition(0, 0),
        Tile.GRASS to TilePosition(5, 0),
        Tile.PLAYER to TilePosition(26, 0),
        Tile.MONSTER to TilePosition(29, 6)
    )

    fun getTileDimensions(tile: Tile) = tilePositionMap[tile]?.let { (column, row) ->
        TileDimensions(
            left = column * tileWidth,
            top = row * tileHeight,
            right = column * tileWidth + tileWidth,
            bottom = row * tileHeight + tileHeight
        )
    } ?: TileDimensions(0, 0, 0, 0)
}