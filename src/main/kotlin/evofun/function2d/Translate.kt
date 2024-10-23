package evofun.function2d

import evofun.geometry.Point
import evofun.random.Dice

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