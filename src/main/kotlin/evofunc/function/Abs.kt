package evofunc.function

import evofunc.geometry.Point
import evofunc.random.Dice
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class Abs(val a: Double = Dice.randomDouble(), val b: Double = Dice.randomDouble()) : PointFunction {
    override fun apply(p: Point): Point {
        return Point(abs(p.x) + a, abs(p.y) + b)
    }

    override fun applyInPlace(p: Point) {
        val x = abs(p.x) + a
        val y = abs(p.y) + b
        p.x = x
        p.y = y
    }

    override fun toString(): String {
        return "Abs()"
    }
}