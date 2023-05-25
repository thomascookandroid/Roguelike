package swing.panels

import game.MapController
import rendering.MapRenderer
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JPanel

class GamePanel : JPanel() {

    private val mapRenderer = MapRenderer()
    private val mapController = MapController()

    override fun paintComponent(graphics: Graphics) {
        super.paintComponent(graphics)
        mapRenderer.renderEntities(mapController.entities, width, height, graphics)
    }

    init {
        preferredSize = Dimension(640, 480)
        background = Color.BLACK
        isFocusable = true
        mapController.start {
            repaint()
        }
    }
}