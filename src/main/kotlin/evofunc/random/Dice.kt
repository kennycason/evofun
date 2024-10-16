package evofunc.random

import kotlin.random.Random

object Dice {
    fun nextInt(n: Int) = Random.nextInt(n)
    fun nextDouble() = Random.nextDouble()
    fun randomDouble(from: Double = -1.0, until: Double = 1.0) = Random.nextDouble(from, until)
    fun nextBoolean() = Random.nextBoolean()
}