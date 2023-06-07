package swing

import javax.swing.JFrame

class Game : JFrame() {
    companion object Renderer {
        private val gamePanel = GamePanel()
        fun render() {
            gamePanel.repaint()
        }
    }

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