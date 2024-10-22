package evofunc.function2d

import evofunc.geometry.Point
import evofunc.random.Dice

data class Translate(
    private val dx: Double = Dice.randomDouble(),
    private val dy: Double = Dice.randomDouble()
) : Function2D {
    override fun apply(p: Point) = Point(
        p.x + dx,
        p.y + dy
    )

    override fun applyInPlace(p: Point) {
        p.x += dx
        p.y += dy
    }
}