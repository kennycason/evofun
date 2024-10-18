import evofunc.bio.Genetic
import evofunc.bio.Organism
import evofunc.geometry.Point
import java.awt.Graphics
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants
import kotlin.random.Random

fun main(args: Array<String>) {
    EvoFuncRandomWalk().run()
}

class EvoFuncRandomWalk {
    // 16:9 512x288
    private val worldWidth = 256
    private val worldHeight = 256
    private val saveOutput = true
    private val saveOutputFrequency = 100
    private val canvas = BufferedImage(worldWidth, worldHeight, BufferedImage.TYPE_INT_ARGB)
    private val canvasGraphics = canvas.graphics
    private var organism = Organism(dna = Genetic.buildDNA(7), worldWidth, worldHeight)

    fun run() {
        var i = 0
        val panel = object : JPanel() {
            init {
                isFocusable = true
                requestFocusInWindow()

                addKeyListener(object : KeyAdapter() {
                    override fun keyPressed(e: KeyEvent) {
                        println("key pressed")
                        if (e.keyCode == KeyEvent.VK_ENTER) {
                            saveCanvasAsImage()
                        }
                        if (e.keyCode == KeyEvent.VK_N) {
                            println("new dna")
                            organism.dna = Genetic.buildDNA(3 + Random.nextInt(5))
                        }
                    }
                })
            }

            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)

                organism.step(500000)
                organism.express(canvasGraphics)
                Genetic.mutateDna(organism.dna, probability = 0.03)

                g.drawImage(canvas, 0, 0, width, height, this)

                if (saveOutput && (i % saveOutputFrequency == 0)) {
                    saveCanvasAsImage()
                }

                i++
            }

            private fun saveCanvasAsImage() {
                val fileName = "/tmp/iteration_${System.currentTimeMillis() / 1000}_$i.png"
                ImageIO.write(canvas, "png", File(fileName))
                println("saved image to $fileName")
            }
        }

        val frame = JFrame()
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(worldWidth * 3, worldHeight * 3 + 18)
        frame.isVisible = true
        frame.add(panel)
        panel.revalidate()

        while (true) {
            panel.repaint()
        }
    }

    fun squashPoint(point: Point, targetWidth: Int, targetHeight: Int, originalMax: Int): Point {
        val scaledX = (point.x / originalMax.toDouble()) * targetWidth
        val scaledY = (point.y / originalMax.toDouble()) * targetHeight
        return Point(scaledX, scaledY)
    }

}
