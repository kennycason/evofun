package evofun

import evofun.bio.Genetic
import evofun.bio.Organism
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

fun main(args: Array<String>) {
    EvoFunRandomWalkTiled().run()
}

class EvoFunRandomWalkTiled {
    // 16:9 512x288
    private val worldWidth = 300
    private val worldHeight = 300
    private val saveOutput = true
    private val saveOutputFrequency = 10
    private val canvas = BufferedImage(worldWidth, worldHeight, BufferedImage.TYPE_INT_ARGB)
    private val canvasGraphics = canvas.graphics
    private val genesCount = 8
    private var organisms = List(9) {
        Organism(dna = Genetic.buildDNA(genesCount), worldWidth, worldHeight)
    }
    private var maxEntropy = Double.MIN_VALUE

    init {
        val gridSize = 3
        val cellWidth = worldWidth / gridSize
        val cellHeight = worldHeight / gridSize

        organisms.forEachIndexed { index, organism ->
            val row = index / gridSize
            val col = index % gridSize

            val x = col * cellWidth
            val y = row * cellHeight

            organism.position.x = x.toDouble()
            organism.position.y = y.toDouble()
        }
    }

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
                    }
                })
            }

            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)

                for (organism in organisms) {
                    organism.step(50000)
                    organism.entropy = Entropy.calculateNormalizedEntropy(organism.buffer)
                    organism.express(canvasGraphics)

                    if (organism.entropy > maxEntropy) {
                        maxEntropy = organism.entropy
                        println("i,maxent: $i, $maxEntropy")
                        Genetic.mutateDna(organism.dna, probability = 0.0)
                    } else {
                        Genetic.mutateDna(organism.dna, probability = 0.1)
                    }
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
                return mutationRate / 4
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
        }
    }

}
