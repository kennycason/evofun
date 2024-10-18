package evofunc

import evofunc.bio.Genetic
import evofunc.bio.Organism
import evofunc.image.ImageDifference
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
    EvoFuncEvolveImage().run()
}

class EvoFuncEvolveImage {
    // 16:9 = 512x288
    private val targetImageFileName = "octopus.png"
    private var targetImage: BufferedImage = ImageIO.read(Thread.currentThread().contextClassLoader.getResource(targetImageFileName))
    private val worldWidth = targetImage.width
    private val worldHeight = targetImage.height
    private val canvas = BufferedImage(worldWidth, worldHeight, BufferedImage.TYPE_INT_ARGB)
    private val canvasGraphics = canvas.graphics
    private val imageDifference = ImageDifference(2)
    private val genesCount = 7
    private var organism = Organism(dna = Genetic.buildDNA(genesCount), worldWidth, worldHeight)
    private var mostFitOrganism = Organism(dna = Genetic.buildDNA(genesCount), worldWidth, worldHeight)
    private var minErrorScore = Double.MAX_VALUE

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
                            organism.dna = Genetic.buildDNA(genesCount)
                        }
                    }
                })
            }

            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)

                organism.step(1000000)
                organism.express(canvasGraphics)

                val error = imageDifference.compare(canvas, targetImage)
                if (error < minErrorScore) {
                    println("$i, $error")
                    minErrorScore = error
                    mostFitOrganism = organism.clone()
                    Genetic.mutateDna(organism.dna, probability = 0.1)
                    saveCanvasAsImage()
                } else {
                    organism = mostFitOrganism.clone() // reset
                    Genetic.mutateDna(organism.dna, probability = 0.1)
                }

                g.drawImage(canvas, 0, 0, width, height, this)

                i++
            }

            private fun saveCanvasAsImage() {
                val fileName = "/tmp/iteration_${System.currentTimeMillis() / 1000}_$i.png"
                ImageIO.write(canvas, "png", File(fileName))
                //println("saved image to $fileName")
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
