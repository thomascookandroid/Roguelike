package swing

import game.GameController
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JPanel

class GamePanel : JPanel() {

    private val gameController = GameController()

    override fun paintComponent(graphics: Graphics) {
        super.paintComponent(graphics)
        gameController.render(graphics, width, height)
    }

    init {
        preferredSize = Dimension(640, 480)
        background = Color.BLACK
        isFocusable = true
        gameController.start()
    }
}