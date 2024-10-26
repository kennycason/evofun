package evofun.function2d

import evofun.geometry.Point
import evofun.random.Dice
import kotlin.math.pow

/**
 * mandelbrot-like transformation using quadratic terms
 */
class MandelbrotLike(
    private val a: Double = Dice.randomDouble(),
    private val b: Double = Dice.randomDouble(),
    private val c: Double = Dice.randomDouble(),
    private val d: Double = Dice.randomDouble(),
    private val e: Double = Dice.randomDouble(),
    private val f: Double = Dice.randomDouble(),
    private val maxIterations: Int = 10,
    private val divergenceThreshold: Double = 20.0
) : Function2D {

    override fun apply(p: Point): Point {
        var x = p.x
        var y = p.y
        var iteration = 0

        while (iteration < maxIterations && x * x + y * y < divergenceThreshold) {
            val newX = a * x.pow(2) - b * y.pow(2) + c * x + d
            val newY = 2 * a * x * y + e * y + f
            x = newX
            y = newY
            iteration++
        }
        return Point(x, y)
    }

    override fun applyInPlace(p: Point) {
        var x = p.x
        var y = p.y
        var iteration = 0
        while (iteration < maxIterations && x * x + y * y < divergenceThreshold) {
            val newX = a * x.pow(2) - b * y.pow(2) + c * x + d
            val newY = 2 * a * x * y + e * y + f

            x = newX
            y = newY
            iteration++
        }
        p.x = x
        p.y = y
    }
}