package swing.frames

import swing.panels.GamePanel
import javax.swing.JFrame

class Game : JFrame() {
    private val gamePanel = GamePanel()

    init {
        add(gamePanel)
        title = "Roguelike"
        isVisible = true
        isResizable = true
        defaultCloseOperation = EXIT_ON_CLOSE
        pack()
        setLocationRelativeTo(null)
    }
}