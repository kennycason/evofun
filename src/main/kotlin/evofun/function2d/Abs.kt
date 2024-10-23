package evofun.function2d

import evofun.geometry.Point
import evofun.random.Dice
import kotlin.math.abs

class Abs(val a: Double = Dice.randomDouble(), val b: Double = Dice.randomDouble()) : Function2D {
    override fun apply(p: Point): Point {
        return Point(abs(p.x) + a, abs(p.y) + b)
    }

    override fun applyInPlace(p: Point) {
        val x = abs(p.x) + a
        val y = abs(p.y) + b
        p.x = x
        p.y = y
    }

}