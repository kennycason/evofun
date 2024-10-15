package evofunc.function

import evofunc.geometry.Point
import evofunc.random.Dice
import kotlin.math.sin
import kotlin.math.tan

class Popcorn(val a: Double = Dice.randomDouble(), val b: Double = Dice.randomDouble()) : PointFunction {
    override fun apply(p: Point): Point {
        return Point(p.x + a * sin(tan(3 * p.y)), p.y + b * sin(tan(3 * p.x)))
    }
}