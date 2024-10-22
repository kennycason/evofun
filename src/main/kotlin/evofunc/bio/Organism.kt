package evofunc.bio

import evofunc.geometry.Point
import evofunc.random.Dice
import evofunc.util.array2d
import java.awt.Color
import java.awt.Graphics

class Organism(
    var dna: DNA,
    val width: Int,
    val height: Int,
    val position: Point = Point(x = 0.0, y = 0.0),
    var entropy: Double = 0.0
) {
    data class Cell(var count: Double = 0.0)
    val buffer: Array<Array<Cell>> = array2d(width, height) { Cell() }
    private var maxCount = 0.0

    fun step(steps: Int) {
        val expressedDna = Genetic.express(dna)

        (0 until width).forEach { x ->
            (0 until height).forEach { y ->
                buffer[x][y].count *= 0.7
                buffer[x][y].count -= 1.0
//                buffer[x][y].count = 0.0
                if (buffer[x][y].count < 0) {
                    buffer[x][y].count = 0.0
                }
            }
        }

        var x = 1 - 2 * Dice.nextDouble()
        var y = 1 - 2 * Dice.nextDouble()
        var skipped = 0
        (0 until steps).forEach { step ->
            val point = when (dna.geneExpressionOrder) {
                DNA.GeneExpressionOrder.RANDOM -> expressedDna.expressed[Dice.nextInt(dna.size)].apply(Point(x, y))
                DNA.GeneExpressionOrder.SEQUENTIAL_ITERATIVE -> expressedDna.expressed[step % dna.size].apply(Point(x, y))
//                DNA.GeneExpressionOrder.SEQUENTIAL_ALL -> expressedDna.expressed.fold(Point(x, y)) { p, gene -> gene.apply(p) }
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
        val expressedDna = Genetic.express(dna)

        (0 until width).forEach { x ->
            (0 until height).forEach { y ->
                canvasGraphics.color = expressedDna.colorFunction.apply(x, y, buffer, maxCount)
                canvasGraphics.fillRect(position.x.toInt() + x, position.y.toInt() + y, 1, 1)
            }
        }
    }

    fun reset() {
        entropy = 0.0
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
        entropy = entropy
    )

}