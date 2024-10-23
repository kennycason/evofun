package evofun.function1d

import evofun.random.Dice
import kotlin.math.exp
import kotlin.math.pow

data class Guassian(
    private val x0: Double = Dice.randomDouble(), private val sigmaX: Double = Dice.randomDouble(),
    private val amplitude: Double = Dice.randomDouble()
) : Function1D {
    override fun apply(x: Double): Double {
        return amplitude * exp(-((x - x0).pow(2)) / (2 * sigmaX.pow(2)))
    }
}