import evofunc.color.Colorizer
import evofunc.genetic.Genetic
import evofunc.geometry.Point
import evofunc.random.Dice
import evofunc.util.array2d
import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants
import kotlin.math.pow

fun main(args: Array<String>) {
    EvoFunc().run()
}

class EvoFunc {
    data class Cell(var count: Double = 0.0)

    private val worldWidth = 512
    private val worldHeight = 512
    private val saveOutput = true
    private val saveOutputFrequency = 100
    private val canvas: BufferedImage = BufferedImage(worldWidth, worldHeight, BufferedImage.TYPE_INT_ARGB)
    private val canvasGraphics = canvas.graphics
    private val buffer = array2d(worldWidth, worldHeight) { Cell() }
    private val DNA = Genetic.buildDNA(8)

    fun run() {
        val frame = JFrame()
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(worldWidth * 3, worldHeight * 3 + 18)
        frame.isVisible = true

        var x = 1 - 2 * Dice.nextDouble()
        var y = 1 - 2 * Dice.nextDouble()
        var maxCount = 0.0

        fun step(steps: Int) {
            var skipped = 0
            (0 until steps).forEach {
                val i = Dice.nextInt(DNA.size)
                val point = DNA.genes[i].function.apply(Point(x, y))
                x = point.x
                y = point.y
                val xInt = ((x + 1) * (worldWidth * .9) / 2.0).toInt()
                val yInt = ((y + 1) * (worldHeight * .9) / 2.0).toInt()
                if (xInt >= 0 && xInt < worldWidth && yInt >= 0 && yInt < worldHeight) {
                    buffer[xInt][yInt].count++
                    if (buffer[xInt][yInt].count > maxCount) {
                        maxCount = buffer[xInt][yInt].count
                    }
                } else {
                    skipped++
                }
            }

        }

        step(10000000)

        val gamma = 0.8
        var i = 0
        val panel = object : JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)

                step(500000)

                canvasGraphics.color = Color.BLACK
                canvasGraphics.clearRect(0, 0, worldWidth, worldHeight)

                (0 until worldWidth).forEach { x ->
                    (0 until worldHeight).forEach { y ->
                        if (buffer[x][y].count > 2) {
//                            val normalized = (buffer[x][y].count / maxCount).pow(gamma)
//                            val rgb = (normalized * 0xFFFFFF).toInt()
//                            canvasGraphics.color = Color(rgb)

                            val rgb = ((buffer[x][y].count / maxCount) * 0xFFFFFF).toInt()
                            canvasGraphics.color = Color(rgb)

                            canvasGraphics.color = DNA.colorizer.apply(buffer[x][y].count, 0xFF)
                        } else {
                            canvasGraphics.color = Color.BLACK
                        }

                        canvasGraphics.fillRect(x, y, 1, 1)
                    }
                }

                g.drawImage(canvas, 0, 0, width, height, this)

                Genetic.mutateDna(DNA, probability = 0.2)

                (0 until worldWidth).forEach { x ->
                    (0 until worldHeight).forEach { y ->
                        buffer[x][y].count *= 0.85
                    }
                }

                if (saveOutput && (i % saveOutputFrequency == 0)) {
                    println("save...")
                    val fileName = "/tmp/iteration_${System.currentTimeMillis() / 1000}_$i.png"
                    ImageIO.write(canvas, "png", File(fileName))
                }

                i++
            }
        }
        frame.add(panel)
        panel.revalidate()

        while (true) {
            panel.repaint()
            Thread.sleep(100)
        }
    }

}

