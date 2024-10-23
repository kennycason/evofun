package evofun.function2d

import evofun.geometry.Point
import evofun.random.Dice
import kotlin.math.cos
import kotlin.math.sin

data class Swirl(
    private val a: Double = Dice.randomDouble(), private val b: Double = Dice.randomDouble(),
    private val c: Double = Dice.randomDouble(), private val d: Double = Dice.randomDouble()
) : Function2D {
    override fun apply(p: Point): Point {
        val r = r(p)
        val rSquared = r * r
        val sinRSquared = sin(rSquared)
        val cosRSquared = cos(rSquared)
        return Point((a * p.x * sinRSquared) - (b * p.y * cosRSquared) + c, (a * p.x * cosRSquared) + (b * p.y * sinRSquared) + d)
    }
    override fun applyInPlace(p: Point) {
        val r = r(p)
        val rSquared = r * r
        val sinRSquared = sin(rSquared)
        val cosRSquared = cos(rSquared)
        val x = (a * p.x * sinRSquared) - (b * p.y * cosRSquared) + c
        val y = (a * p.x * cosRSquared) + (b * p.y * sinRSquared) + d
        p.x = x
        p.y = y
    }
}