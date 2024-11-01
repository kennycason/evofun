package evofun

import evofun.bio.DNA
import evofun.bio.DNAFarm
import evofun.bio.Genetic
import evofun.bio.Organism
import evofun.image.Entropy
import evofun.random.Dice
import evofun.util.padWithZeros
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

fun main(args: Array<String>) {
    EvoFunHalloweenPartyDNAFarm().run()
}

class EvoFunHalloweenPartyDNAFarm {
    // 16:9 512x288
    private val worldWidth = 640
    private val worldHeight = 360
    private val scale = 2
    private val canvas = BufferedImage(worldWidth, worldHeight, BufferedImage.TYPE_INT_ARGB)
    private val canvasGraphics = canvas.graphics
    private var turnsEntropyIsBelowThreshold = 0
    private var turnsSinceEntropyAboveThreshold = 0
    private var mutationRate = 0.05
    private val saveImage = true
    private val imageFolderBase = "/tmp/evofun_${System.currentTimeMillis() / 1000}/"
    private val dnaFarm = DNAFarm()
    private val maxGeneCount = 50
    private var organism = loadRandomOrganism()

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
                            println("build new random dna")
                            organism = Organism(dna = Genetic.buildDNA(Dice.nextInt(maxGeneCount) + 1), worldWidth, worldHeight)
                            turnsSinceEntropyAboveThreshold = 0
                        }
                        if (e.keyCode == KeyEvent.VK_B) {
                            println("reproduce new dna from farm")
                            organism = birthNewOrganism()
                            turnsSinceEntropyAboveThreshold = 0
                        }
                        if (e.keyCode == KeyEvent.VK_R) {
                            println("load random dna from farm")
                            organism = loadRandomOrganism()
                            turnsSinceEntropyAboveThreshold = 0
                        }
                        if (e.keyCode == KeyEvent.VK_S) {
                            println("save dna")
                            dnaFarm.writeDNA(organism.dna, canvas)
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
//                ImageToGrayScaleBuffer.addToBuffer(targetImage, 0.4, organism.buffer)

                organism.express(canvasGraphics)
                organism.entropy = Entropy.calculateNormalizedEntropy(canvas)

                if (organism.entropy <= 0.01) {
                    turnsEntropyIsBelowThreshold++
                    turnsSinceEntropyAboveThreshold = 0
                } else {
                    turnsEntropyIsBelowThreshold = 0
                    turnsSinceEntropyAboveThreshold++
                }

                if (turnsEntropyIsBelowThreshold > 5) {
                    println("reset because boring")
                    organism = birthNewOrganism()
                } else if (turnsSinceEntropyAboveThreshold >= 100) {
                    println("reset because pattern hit 100 iterations")
                    organism = birthNewOrganism()
                    turnsSinceEntropyAboveThreshold = 0
                } else {
                    mutationRate = getMutationRate(organism.entropy, minRate = 0.005, maxRate = 0.0075)
                    Genetic.mutateDna(organism.dna, probability = mutationRate)
                }

                println("$i, ${organism.entropy}, $mutationRate")
                g.drawImage(canvas, 0, 0, width, height, this)
                if (saveImage) saveCanvasAsImage()
                i++
//                println(organism.dna.genes.joinToString { it.function.name })
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
                val file = File(imageFolderBase)
                if (!file.exists()) {
                    println("Creating organism folder: $imageFolderBase")
                    file.mkdirs()
                }
                val fileName = "$imageFolderBase${i.padWithZeros(5)}.png"
                ImageIO.write(canvas, "png", File(fileName))
                // println("saved image to $fileName")
            }
        }

        val frame = JFrame()
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(worldWidth * scale, (worldHeight * scale) + 18)
        frame.isVisible = true
        frame.add(panel)
        panel.revalidate()

        while (true) {
            panel.repaint()
            sleep(20)
        }
    }

    private fun loadRandomOrganism(): Organism {
        val dnaFormFarm = dnaFarm.readRandomDNA()
        dnaFormFarm ?: return Organism(dna = Genetic.buildDNA(Dice.nextInt(maxGeneCount) + 1), worldWidth, worldHeight)
        println("read dna from farm")
        return Organism(dna = dnaFormFarm, worldWidth, worldHeight)
    }

    private fun birthNewOrganism(): Organism {
        val organismA = loadRandomOrganism()
        val organismB = loadRandomOrganism()
        val clone: DNA = Genetic.reproduce(organismA.dna, organismB.dna)
        println("----")
        println(organismA.dna)
        println(organismB.dna)
        println(clone)
        println("----")
        return Organism(dna = clone, worldWidth, worldHeight, entropy = (organismA.entropy + organismB.entropy) / 2)
    }

}
