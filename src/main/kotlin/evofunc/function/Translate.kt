package evofunc.function

import evofunc.geometry.Point
import evofunc.random.Dice
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.tan

data class Translate(
    private val dx: Double = Dice.randomDouble(),
    private val dy: Double = Dice.randomDouble()
) : PointFunction {
    override fun apply(p: Point) = Point(
        p.x + dx,
        p.y + dy
    )

    override fun applyInPlace(p: Point) {
        p.x += dx
        p.y += dy
    }
}