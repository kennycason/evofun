package evofunc.function2d

import evofunc.geometry.Point
import evofunc.random.Dice
import kotlin.math.cos
import kotlin.math.sin

data class Rotate(
    private val theta: Double = Dice.randomDouble(),
    private val centerX: Double = 0.0,
    private val centerY: Double = 0.0
) : Function2D {
    override fun apply(p: Point) = Point(
        cos(theta) * (p.x - centerX) - sin(theta) * (p.y - centerY) + centerX,
        sin(theta) * (p.x - centerX) + cos(theta) * (p.y - centerY) + centerY
    )

    override fun applyInPlace(p: Point) {
        val x = cos(theta) * (p.x - centerX) - sin(theta) * (p.y - centerY) + centerX
        val y = sin(theta) * (p.x - centerX) + cos(theta) * (p.y - centerY) + centerY
        p.x = x
        p.y = y
    }
}