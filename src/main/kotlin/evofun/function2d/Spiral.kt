package evofun.function2d

import evofun.geometry.Point
import evofun.random.Dice
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class Spiral(
    private val a: Double = Dice.randomDouble(), private val b: Double = Dice.randomDouble(),
    private val c: Double = Dice.randomDouble(), private val d: Double = Dice.randomDouble()
) : Function2D {
    override fun apply(p: Point): Point {
        // convert to polar coordinates
        val r = sqrt(p.x * p.x + p.y * p.y)
        val theta = atan2(p.y, p.x)

        // create the spiral
        val newR = r * a // originally 0.1
        val newTheta = theta + r * b // 0.05

        // convert back to cartesian coordinates
        val newX = newR * cos(newTheta)
        val newY = newR * sin(newTheta)

        return Point(newX, newY)
    }
    override fun applyInPlace(p: Point) {
        // convert to polar coordinates
        val r = sqrt(p.x * p.x + p.y * p.y)
        val theta = atan2(p.y, p.x)

        // create the spiral
        val newR = r * a // originally 0.1
        val newTheta = theta + r * b // 0.05

        // convert back to cartesian coordinates
        val newX = newR * cos(newTheta)
        val newY = newR * sin(newTheta)
        p.x = newX
        p.y = newY
    }
}