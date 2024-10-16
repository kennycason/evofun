package evofunc.function

import evofunc.geometry.Point
import evofunc.random.Dice
import kotlin.math.sin
import kotlin.math.tan

data class SinSin(
    private val a: Double = Dice.randomDouble(), private val b: Double = Dice.randomDouble(), private val c: Double = Dice.randomDouble(),
    private val d: Double = Dice.randomDouble(), private val e: Double = Dice.randomDouble(), private val f: Double = Dice.randomDouble()
) : PointFunction {
    override fun apply(p: Point) = Point(a * sin(b * p.x) + c, d * sin(e * p.y) + f)
    override fun applyInPlace(p: Point) {
        val x = a * sin(b * p.x) + c
        val y = d * sin(e * p.y) + f
        p.x = x
        p.y = y
    }
}