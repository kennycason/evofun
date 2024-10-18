package evofunc.function

import evofunc.geometry.Point
import evofunc.random.Dice
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.tan

data class Guassian(
    private val x0: Double = Dice.randomDouble(), private val y0: Double = Dice.randomDouble(),
    private val sigmaX: Double = Dice.randomDouble(), private val sigmaY: Double = Dice.randomDouble(),
    private val amplitude: Double = Dice.randomDouble()
) : PointFunction {
    override fun apply(p: Point) = Point(
        amplitude * exp(-((p.x - x0).pow(2)) / (2 * sigmaX.pow(2))),
        amplitude * exp(-((p.y - y0).pow(2)) / (2 * sigmaY.pow(2)))
    )
    override fun applyInPlace(p: Point) {
        val x = amplitude * exp(-((p.x - x0).pow(2)) / (2 * sigmaX.pow(2)))
        val y = amplitude * exp(-((p.y - y0).pow(2)) / (2 * sigmaY.pow(2)))
        p.x = x
        p.y = y
    }
}