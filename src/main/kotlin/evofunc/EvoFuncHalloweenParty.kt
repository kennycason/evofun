package evofunc

import evofunc.bio.Genetic
import evofunc.bio.Organism
import evofunc.image.Entropy
import evofunc.random.Dice
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
    EvoFuncHalloweenParty().run()
}

class EvoFuncHalloweenParty {
    // 16:9 512x288
    private val worldWidth = 640
    private val worldHeight = 360
    private val canvas = BufferedImage(worldWidth, worldHeight, BufferedImage.TYPE_INT_ARGB)
    private val canvasGraphics = canvas.graphics
    private val genesCount = 7
    private var organism = buildOrganism()
    private var mostFitOrganism = buildOrganism()
    private var maxEntropy = Double.MIN_VALUE
    private var turnsEntropyIsBelowThreshold = 0
    private var mutationRate = 0.05

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
                            mostFitOrganism = buildOrganism()
                            organism = organism.clone()
                        }
                    }
                })
            }

            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)

                //println(organism.dna)
                organism.step(1000000)
                organism.express(canvasGraphics)
                organism.entropy = Entropy.calculateNormalizedEntropy(canvas)

                if (organism.entropy <= 0.05) {
                    turnsEntropyIsBelowThreshold++
                }

                if (turnsEntropyIsBelowThreshold > 10 || i % 100 == 0) {
                    if (i % 100 == 0) {
                        println("reset because hit 100 iterations")
                        // render most fit image of cycle
                        mostFitOrganism.step(200000)
                        mostFitOrganism.express(canvasGraphics)
                    //    saveCanvasAsImage()
                    } else {
                        println("reset because boring")
                    }
                    turnsEntropyIsBelowThreshold = 0
                    maxEntropy = Double.MIN_VALUE
                    mostFitOrganism = buildOrganism()
                    organism = mostFitOrganism.clone()
                    mutationRate = Dice.nextDouble() / 10.0

                } else if (organism.entropy >= maxEntropy) {
                    println("new most fit organism")
                    maxEntropy = organism.entropy
                    mostFitOrganism = organism.clone()
                    // saveCanvasAsImage()
                    //val mutationRate = getMutationRate(organism.entropy, maxRate = 0.2) / 4.0
                    Genetic.mutateDna(organism.dna, probability = mutationRate)
                } else {
                    organism = mostFitOrganism.clone() // prep for next iteration
                 //   val mutationRate = getMutationRate(organism.entropy, maxRate = 0.2) / 4.0
                    Genetic.mutateDna(organism.dna, probability = mutationRate)
                }
                println("$i, ${organism.entropy}, $mutationRate")
                g.drawImage(canvas, 0, 0, width, height, this)

                saveCanvasAsImage()

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
//            sleep(2000)
        }
    }

    private fun buildOrganism() = Organism(dna = Genetic.buildDNA(genesCount), worldWidth, worldHeight)

}
