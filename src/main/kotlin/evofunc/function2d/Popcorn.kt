package evofunc.function2d

import evofunc.geometry.Point
import evofunc.random.Dice
import kotlin.math.sin
import kotlin.math.tan

data class Popcorn(val a: Double = Dice.randomDouble(), val b: Double = Dice.randomDouble()) : Function2D {
    override fun apply(p: Point): Point {
        return Point(p.x + a * sin(tan(3 * p.y)), p.y + b * sin(tan(3 * p.x)))
    }
    override fun applyInPlace(p: Point) {
        val x = p.x + a * sin(tan(3 * p.y))
        val y = p.y + b * sin(tan(3 * p.x))
        p.x = x
        p.y = y
    }
}