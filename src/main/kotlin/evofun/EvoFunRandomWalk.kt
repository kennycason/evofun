package evofun

import evofun.bio.Genetic
import evofun.bio.Organism
import evofun.geometry.Point
import evofun.image.Entropy
import evofun.util.padWithZeros
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
    EvoFunRandomWalk().run()
}

class EvoFunRandomWalk {
    // 16:9 512x288
    private val worldWidth = 512
    private val worldHeight = 512
    private val saveOutput = true
    private val saveOutputFrequency = 10
    private val canvas = BufferedImage(worldWidth, worldHeight, BufferedImage.TYPE_INT_ARGB)
    private val canvasGraphics = canvas.graphics
    private val genesCount = 25
    private var organism =
        Organism(dna = Genetic.buildDNA(genesCount), worldWidth, worldHeight)

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

                organism.step(1000000)
                organism.express(canvasGraphics)
               // println(organism.dna)

                val normalizedEntropy = Entropy.calculateNormalizedEntropy(canvas)
                if (normalizedEntropy < 0.05) {
                    Genetic.mutateDna(organism.dna, probability = 0.2)
                } else {
                    Genetic.mutateDna(organism.dna, probability = 0.2)
                }

                if (saveOutput && (i > 0 && (i % saveOutputFrequency == 0))) {
                    saveCanvasAsImage()
                }

                g.drawImage(canvas, 0, 0, width, height, this)

                i++
            }

            /*
             * normalized entropy between 0.0 and 1.0
             * Scale mutation rate based on normalized entropy
             * Higher entropy means lower mutation rate (more interesting images need less mutation)
             *
             */
            private fun getMutationRate(normalizedEntropy: Double, minRate: Double = 0.01, maxRate: Double = 1.0): Double {
                val mutationRate = minRate + (1.0 - normalizedEntropy) * (maxRate - minRate)
                //println("entropy: $normalizedEntropy, mutation: $mutationRate")
                return mutationRate
            }

            private fun saveCanvasAsImage() {
                val fileName = "/tmp/iteration_${System.currentTimeMillis() / 1000}_${i.padWithZeros(5)}.png"
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
//            sleep(2000)
        }
    }

    fun squashPoint(point: Point, targetWidth: Int, targetHeight: Int, originalMax: Int): Point {
        val scaledX = (point.x / originalMax.toDouble()) * targetWidth
        val scaledY = (point.y / originalMax.toDouble()) * targetHeight
        return Point(scaledX, scaledY)
    }

}
