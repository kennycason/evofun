package evofunc.bio

import evofunc.geometry.Point
import evofunc.image.ImageEntropy
import evofunc.random.Dice
import evofunc.util.array2d
import java.awt.Color
import java.awt.Graphics
import kotlin.math.ln

class Organism(
    var dna: DNA,
    val width: Int,
    val height: Int,
    val geneExpressionOrder: GeneExpressionOrder = GeneExpressionOrder.SEQUENTIAL_ITERATIVE,
    val position: Point = Point(x = 0.0, y = 0.0),
    var entropy: Double = 0.0
) {
    enum class GeneExpressionOrder {
        RANDOM,
        SEQUENTIAL_ITERATIVE,
        SEQUENTIAL_ALL
    }
    data class Cell(var count: Double = 0.0)
    private val buffer: Array<Array<Cell>> = array2d(width, height) { Cell() }

    fun step(steps: Int) {
        val expressedDna = Genetic.express(dna)

        (0 until width).forEach { x ->
            (0 until height).forEach { y ->
                buffer[x][y].count *= 0.7
                buffer[x][y].count -= 2.0
//                buffer[x][y].count = 0.0
                if (buffer[x][y].count < 0) {
                    buffer[x][y].count = 0.0
                }
            }
        }

        var x = 1 - 2 * Dice.nextDouble()
        var y = 1 - 2 * Dice.nextDouble()
        var maxCount = 0.0
        var skipped = 0
        (0 until steps).forEach { step ->
            val point = when (geneExpressionOrder) {
                GeneExpressionOrder.RANDOM -> expressedDna.expressed[Dice.nextInt(dna.size)].apply(Point(x, y))
                GeneExpressionOrder.SEQUENTIAL_ITERATIVE -> expressedDna.expressed[step % dna.size].apply(Point(x, y))
                GeneExpressionOrder.SEQUENTIAL_ALL -> expressedDna.expressed.fold(Point(x, y)) { p, gene -> gene.apply(p) }
            }
            x = point.x
            y = point.y
            val xInt = ((x + 1) * (width - 1) / 2.0).toInt()
            val yInt = ((y + 1) * (height - 1) / 2.0).toInt()
            if (xInt >= 0 && xInt < width && yInt >= 0 && yInt < height) {
                buffer[xInt][yInt].count++
                if (buffer[xInt][yInt].count > maxCount) {
                    maxCount = buffer[xInt][yInt].count
                }
            } else {
                skipped++
            }
        }
        // println("skipped: $skipped")
    }

    fun express(canvasGraphics: Graphics) {
        canvasGraphics.color = Color.BLACK
        canvasGraphics.clearRect(position.x.toInt(), position.y.toInt(), width, height)
        (0 until width).forEach { x ->
            (0 until height).forEach { y ->
                if (buffer[x][y].count > 0) {
                    canvasGraphics.color = dna.colorizer.apply(buffer[x][y].count)
                } else {
                    canvasGraphics.color = Color.BLACK
                }
                canvasGraphics.fillRect(position.x.toInt() + x, position.y.toInt() + y, 1, 1)
            }
        }
    }

    fun updateEntropy() {
        this.entropy = ImageEntropy.calculateEntropy(buffer)
    }

    fun reset() {
        (0 until width).forEach { x ->
            (0 until height).forEach { y ->
                buffer[x][y].count = 0.0
            }
        }
    }

    fun clone() = Organism(
        dna = Genetic.clone(dna),
        width = width,
        height = height,
        geneExpressionOrder = geneExpressionOrder,
        entropy = entropy
    )

}