package evofunc.function2d

import evofunc.geometry.Point
import evofunc.random.Dice
import kotlin.math.cos
import kotlin.math.sin

data class Pdj(
    val a: Double = Dice.randomDouble(), val b: Double = Dice.randomDouble(),
    val c: Double = Dice.randomDouble(), val d: Double = Dice.randomDouble()
) : Function2D {
    override fun apply(p: Point): Point {
        return Point(sin(a * p.y) - cos(b * p.x), sin(c * p.x) - cos(d * p.y))
    }
    override fun applyInPlace(p: Point) {
        val x = sin(a * p.y) - cos(b * p.x)
        val y = sin(c * p.x) - cos(d * p.y)
        p.x = x
        p.y = y
    }
}