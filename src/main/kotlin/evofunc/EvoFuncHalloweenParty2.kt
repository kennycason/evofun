package evofunc

import evofunc.bio.Genetic
import evofunc.bio.Organism
import evofunc.image.Entropy
import evofunc.random.Dice
import java.awt.Color
import java.awt.Graphics
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.io.File
import java.lang.Thread.sleep
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants
import kotlin.random.Random

fun main(args: Array<String>) {
    EvoFuncHalloweenParty2().run()
}

class EvoFuncHalloweenParty2 {
    // 16:9 512x288
    private val worldWidth = 640
    private val worldHeight = 360
    private val canvas = BufferedImage(worldWidth, worldHeight, BufferedImage.TYPE_INT_ARGB)
    private val canvasGraphics = canvas.graphics
    private val genesCount = 10
    private var organism = buildOrganism()
    private var turnsEntropyIsBelowThreshold = 0
    private var turnsSinceEntropyAboveThreshold = 0
    private var mutationRate = 0.05
    private val saveImage = true

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
                            organism = buildOrganism()
                        }
                    }
                })
            }

            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)

                canvasGraphics.color = Color.BLACK
                canvasGraphics.clearRect(0, 0, width, height)

                //println(organism.dna)
                organism.step(1000000)
                organism.express(canvasGraphics)
                organism.entropy = Entropy.calculateNormalizedEntropy(canvas)

                if (organism.entropy <= 0.05) {
                    turnsEntropyIsBelowThreshold++
                    turnsSinceEntropyAboveThreshold = 0
                } else {
                    turnsEntropyIsBelowThreshold = 0
                    turnsSinceEntropyAboveThreshold++
                }

                if (turnsEntropyIsBelowThreshold > 2) {
                    println("reset because boring")
                    Genetic.mutateDna(organism.dna, probability = 0.5)
                    organism.reset()
                } else if (turnsSinceEntropyAboveThreshold >= 50) {
                    println("reset because pattern hit 75 iterations")
                    Genetic.mutateDna(organism.dna, probability = 0.5)
                    organism.reset()
                } else {
                    mutationRate = getMutationRate(organism.entropy, minRate = 0.01, maxRate = 0.03)
                    Genetic.mutateDna(organism.dna, probability = mutationRate)
                }

                println("$i, ${organism.entropy}, $mutationRate")
                g.drawImage(canvas, 0, 0, width, height, this)
                if (saveImage) saveCanvasAsImage()
                i++
            }

            /*
             * normalized entropy between 0.0 and 1.0
             * Scale mutation rate based on normalized entropy
             * Higher entropy means lower mutation rate (more interesting images need less mutation)
             *
             */
            private fun getMutationRate(normalizedEntropy: Double, minRate: Double = 0.01, maxRate: Double = 1.0): Double {
                return minRate + ((1.0 - normalizedEntropy) * (maxRate - minRate) / 10.0)
            }

            private fun saveCanvasAsImage() {
                val fileName = "/tmp/halloween_demo_${System.currentTimeMillis() / 1000}_$i.png"
                ImageIO.write(canvas, "png", File(fileName))
                println("saved image to $fileName")
            }
        }

        val frame = JFrame()
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(worldWidth * 2, worldHeight * 2 + 18)
        frame.isVisible = true
        frame.add(panel)
        panel.revalidate()

        while (true) {
            panel.repaint()
            sleep(20)
        }
    }

    private fun buildOrganism() =
        Organism(dna = Genetic.buildDNA(genesCount), worldWidth, worldHeight)

}
