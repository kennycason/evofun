package evofun.function2d

import evofun.geometry.Point
import evofun.random.Dice

data class Scale(
    private val scaleX: Double = Dice.randomDouble(),
    private val scaleY: Double = Dice.randomDouble()
) : Function2D {
    override fun apply(p: Point) = Point(
        p.x * scaleX,
        p.y * scaleY
    )

    override fun applyInPlace(p: Point) {
        p.x *= scaleX
        p.y *= scaleY
    }
}