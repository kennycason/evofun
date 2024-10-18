package evofunc.bio

import evofunc.geometry.Point
import evofunc.random.Dice
import evofunc.util.array2d
import java.awt.Color
import java.awt.Graphics

class Organism(
    var dna: DNA,
    private val width: Int,
    private val height: Int
) {
    private data class Cell(var count: Double = 0.0)
    private val buffer: Array<Array<Cell>> = array2d(width, height) { Cell() }

    fun step(steps: Int) {
        val expressedDna = Genetic.express(dna)

        val fadeAmount = 2.0 // Dice.nextDouble() * 10.0
        (0 until width).forEach { x ->
            (0 until height).forEach { y ->
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
//            val point = expressedDna.expressed.fold(Point(x, y)) { p, gene -> gene.apply(p) }
//            val point = Point(x, y)
//            expressedDna.expressed.map { gene -> gene.applyInPlace(point) }
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
        canvasGraphics.clearRect(0, 0, width, height)

        (0 until width).forEach { x ->
            (0 until height).forEach { y ->
                if (buffer[x][y].count > 0) {
                    canvasGraphics.color = dna.colorizer.apply(buffer[x][y].count)
                } else {
                    canvasGraphics.color = Color.BLACK
                }
                canvasGraphics.fillRect(x, y, 1, 1)
            }
        }
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
        height = height
    )

}