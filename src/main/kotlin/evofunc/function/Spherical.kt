package evofunc.function

import evofunc.geometry.Point
import evofunc.random.Dice
import kotlin.math.sin

data class Spherical(
    private val a: Double = Dice.randomDouble(), private val b: Double = Dice.randomDouble(),
    private val c: Double = Dice.randomDouble(), private val d: Double = Dice.randomDouble()
) : PointFunction {
    override fun apply(p: Point): Point {
        val r = r(p)
        val rSquared = r * r
        return Point(a * p.x / rSquared + b, c * p.y / rSquared + d)
    }
    override fun applyInPlace(p: Point) {
        val r = r(p)
        val rSquared = r * r
        val x = a * p.x / rSquared + b
        val y = c * p.y / rSquared + d
        p.x = x
        p.y = y
    }
}