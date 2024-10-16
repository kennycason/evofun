package evofunc.function

import evofunc.geometry.Point
import evofunc.random.Dice
import kotlin.math.abs

data class Horseshoe(
    private val a: Double = Dice.randomDouble(), private val b: Double = Dice.randomDouble(),
    private val c: Double = Dice.randomDouble(), private val d: Double = Dice.randomDouble()
) : PointFunction {
    override fun apply(p: Point): Point {
        val r = r(p)
        return Point(a * ((p.x - p.y) * (p.x + p.y)) / r + b, c * (2 * p.x * p.y) / r + d)
    }
    override fun applyInPlace(p: Point) {
        val r = r(p)
        val x = a * ((p.x - p.y) * (p.x + p.y)) / r + b
        val y = c * (2 * p.x * p.y) / r + d
        p.x = x
        p.y = y
    }
}