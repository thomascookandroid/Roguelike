package swing.panels

import game.GameController
import tiles.TileSet
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JPanel

class GamePanel : JPanel() {

    private val gameController = GameController()

    private val tileSet = TileSet(
        tilesetPath = "../tileset.png"
    )

    override fun paintComponent(graphics: Graphics) {
        super.paintComponent(graphics)
        val tileWidth = width / 20
        val tileHeight = height / 20
//        gameController.entities.forEach { entity ->
//            val tileDimensions = tileSet.getTileDimensions(entity.tile)
//            graphics.drawImage(
//                tileSet.image,
//                entity.position.value.x * tileWidth,
//                entity.position.value.y * tileHeight,
//                entity.position.value.x * tileWidth + tileWidth,
//                entity.position.value.y * tileHeight + tileHeight,
//                tileDimensions.left,
//                tileDimensions.top,
//                tileDimensions.right,
//                tileDimensions.bottom,
//                null
//            )
//        }
        val costGrid = gameController.getCostGrid()
        graphics.color = Color.WHITE
        costGrid.forEachIndexed { x, rows ->
            rows.forEachIndexed { y, cell ->
                graphics.drawString(
                    cell.toString(),
                    x * tileWidth + tileWidth / 2,
                    y * tileHeight + tileHeight / 2
                )
            }
        }
    }

    init {
        preferredSize = Dimension(640, 480)
        background = Color.BLACK
        isFocusable = true
        gameController.start {
            repaint()
        }
    }
}