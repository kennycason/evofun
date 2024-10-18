package evofunc.function

import evofunc.geometry.Point
import evofunc.random.Dice
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.tan

data class Scale(
    private val scaleX: Double = Dice.randomDouble(),
    private val scaleY: Double = Dice.randomDouble()
) : PointFunction {
    override fun apply(p: Point) = Point(
        p.x * scaleX,
        p.y * scaleY
    )

    override fun applyInPlace(p: Point) {
        p.x *= scaleX
        p.y *= scaleY
    }
}