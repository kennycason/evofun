import evofunc.genetic.Genetic
import evofunc.geometry.Point
import evofunc.random.Dice
import evofunc.util.array2d
import java.awt.Color
import java.awt.Graphics
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants
import kotlin.random.Random

fun main(args: Array<String>) {
    EvoFunc().run()
}

class EvoFunc {
    data class Cell(var count: Double = 0.0)

    private val worldWidth = 512
    private val worldHeight = 512 // 288
    private val saveOutput = true
    private val saveOutputFrequency = 10
    private val canvas: BufferedImage = BufferedImage(worldWidth, worldHeight, BufferedImage.TYPE_INT_ARGB)
    private val canvasGraphics = canvas.graphics
    private val buffer = array2d(worldWidth, worldHeight) { Cell() }
    private var dna = Genetic.buildDNA(6)

    fun squashPoint(point: Point, targetWidth: Int, targetHeight: Int, originalMax: Int): Point {
        val scaledX = (point.x / originalMax.toDouble()) * targetWidth
        val scaledY = (point.y / originalMax.toDouble()) * targetHeight
        return Point(scaledX, scaledY)
    }

    fun run() {
        fun step(steps: Int) {
            val expressedDna = Genetic.express(dna)
            val fadeAmount = Dice.nextDouble() * 10.0
            (0 until worldWidth).forEach { x ->
                (0 until worldHeight).forEach { y ->
                    buffer[x][y].count -= fadeAmount
                    if (buffer[x][y].count < 0) {
                        buffer[x][y].count = 0.0
                    }
                }
            }

            var x = 1 - 2 * Dice.nextDouble()
            var y = 1 - 2 * Dice.nextDouble()
            var maxCount = 0.0
            var skipped = 0
            (0 until steps).forEach {
                val i = Dice.nextInt(dna.size)
                val point = expressedDna.expressed[i].apply(Point(x, y))
//                val point = expressedDna.expressed.fold(Point(x, y)) { p, gene -> gene.apply(p) }

//                val point = Point(x, y)
//                expressedDna.expressed.map { gene -> gene.applyInPlace(point) }

                x = point.x
                y = point.y
                val xInt = ((x + 1) * (worldWidth-1) / 2.0).toInt()
                val yInt = ((y + 1) * (worldHeight-1) / 2.0).toInt()
                if (xInt >= 0 && xInt < worldWidth && yInt >= 0 && yInt < worldHeight) {
                    buffer[xInt][yInt].count++
                    if (buffer[xInt][yInt].count > maxCount) {
                        maxCount = buffer[xInt][yInt].count
                    }
                } else {
                    skipped++
                    // TODO scale function in
                }
            }
            // println("skipped: $skipped")

            // println("----")
            Genetic.mutateDna(dna, probability = 0.01)
            //println(dna)
            //println("----")
        }

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
                            dna = Genetic.buildDNA(3 + Random.nextInt(5))
                        }
                    }
                })
            }

            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)

                step(1000000)

                canvasGraphics.color = Color.BLACK
                canvasGraphics.clearRect(0, 0, worldWidth, worldHeight)

                (0 until worldWidth).forEach { x ->
                    (0 until worldHeight).forEach { y ->
                        if (buffer[x][y].count > 0) {
                            canvasGraphics.color = dna.colorizer.apply(buffer[x][y].count)
                        } else {
                            canvasGraphics.color = Color.BLACK
                        }

                        canvasGraphics.fillRect(x, y, 1, 1)
                    }
                }

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
          //  Thread.sleep(100)
        }
    }

}

//                            val normalized = (buffer[x][y].count / maxCount).pow(gamma)
//                            val rgb = (normalized * 0xFFFFFF).toInt()
//                            canvasGraphics.color = Color(rgb)
//
//                            val rgb = ((buffer[x][y].count / maxCount) * 0xFFFFFF).toInt()
//                            canvasGraphics.color = Color(rgb)
//                            val ratio = buffer[x][y].count / maxCount.toDouble()
//                            val red = (ratio * 255).toInt()
//                            val green = (ratio * 128).toInt()  // keep green low to favor red
//                            val blue = (ratio * 64).toInt()    // keep blue even lower
//                            val rgb = (red shl 16) or (green shl 8) or blue
//                            canvasGraphics.color = Color(rgb)