package evofun

import evofun.bio.Genetic
import evofun.bio.Organism
import evofun.image.Entropy
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
    evofun.EvoFuncEvolveEntropy().run()
}

class EvoFuncEvolveEntropy {
    // 16:9 512x288
    private val worldWidth = 512
    private val worldHeight = 512
    private val canvas = BufferedImage(worldWidth, worldHeight, BufferedImage.TYPE_INT_ARGB)
    private val canvasGraphics = canvas.graphics
    private val genesCount = 7
    private var organism = Organism(dna = Genetic.buildDNA(genesCount), worldWidth, worldHeight)
    private var mostFitOrganism = Organism(dna = Genetic.buildDNA(genesCount), worldWidth, worldHeight)
    private var maxEntropy = Double.MIN_VALUE
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

                organism.entropy = Entropy.calculateNormalizedEntropy(canvas)
                if (organism.entropy >= maxEntropy) {
                    maxEntropy = organism.entropy
                    mostFitOrganism = organism.clone()
                    saveCanvasAsImage()
                } else {
                    organism = mostFitOrganism.clone() // reset
                }

                val mutationRate = getMutationRate(organism.entropy, maxRate = 0.2)
                Genetic.mutateDna(organism.dna, probability = mutationRate / 4)

                println("$i, ${organism.entropy}, $mutationRate")

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
                return minRate + ((1.0 - normalizedEntropy) * (maxRate - minRate) / 10.0)
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
//            sleep(2000)
        }
    }

}
