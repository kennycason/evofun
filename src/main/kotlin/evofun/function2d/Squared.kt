package evofun.function2d

import evofun.geometry.Point
import evofun.random.Dice

data class Squared(
    private val a: Double = Dice.randomDouble(), private val b: Double = Dice.randomDouble(),
    private val c: Double = Dice.randomDouble(), private val d: Double = Dice.randomDouble()
) : Function2D {
    override fun apply(p: Point) = Point(a * (p.x * p.x) + b, c * (p.y * p.y))
    override fun applyInPlace(p: Point) {
        val x = a * (p.x * p.x) + b
        val y = c * (p.y * p.y) + c
        p.x = x
        p.y = y
    }
}