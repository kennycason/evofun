package evofun.function1d

import evofun.random.Dice
import kotlin.math.abs

class Abs(val a: Double = Dice.randomDouble(), val b: Double = Dice.randomDouble()) : Function1D {
    override fun apply(x: Double): Double {
        return a * abs(x) + b
    }
}