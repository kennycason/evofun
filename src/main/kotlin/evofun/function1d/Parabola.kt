package evofun.function1d

import evofun.random.Dice
import kotlin.math.pow

data class Parabola(
    private val a: Double = Dice.randomDouble(), private val b: Double = Dice.randomDouble(), private val c: Double = Dice.randomDouble()
) : Function1D {
    override fun apply(x: Double) = a * x.pow(b) + c

}