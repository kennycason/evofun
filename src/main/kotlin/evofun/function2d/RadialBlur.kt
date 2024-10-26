package evofun.function2d

import evofun.geometry.Point
import evofun.random.Dice
import kotlin.math.exp

class RadialBlur(
    private val a: Double = Dice.randomDouble(),
    private val b: Double = Dice.randomDouble(),
    private val intensity: Double = Dice.randomDouble(0.5, 1.5)
) : Function2D {

    override fun apply(p: Point): Point {
        val newP = Point(p.x, p.y)
        applyInPlace(newP)
        return newP
    }

    override fun applyInPlace(p: Point) {
        val r = r(p)
        val factor = a * exp(-intensity * r) + b
        p.x *= factor
        p.y *= factor
    }
}