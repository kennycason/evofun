package evofunc.function2d

import evofunc.geometry.Point
import evofunc.random.Dice
import kotlin.math.sin
import kotlin.math.pow
import kotlin.math.sqrt

data class Deformation(
    // Parameters to control various types of deformation
    private val frequencyX: Double = Dice.randomDouble(),
    private val frequencyY: Double = Dice.randomDouble(),
    private val amplitudeX: Double = Dice.randomDouble(),
    private val amplitudeY: Double = Dice.randomDouble(),
    private val radialEffect: Double = Dice.randomDouble(),
    private val noiseFactor: Double = Dice.randomDouble()
) : Function2D {

    override fun apply(p: Point) = Point(
        deformX(p),
        deformY(p)
    )

    override fun applyInPlace(p: Point) {
        p.x = deformX(p)
        p.y = deformY(p)
    }

    // X-coordinate deformation
    private fun deformX(p: Point): Double {
        // Sinusoidal deformation based on y-coordinate
        val sinDeformation = amplitudeX * sin(frequencyX * p.y)
        // Radial deformation, pushing points outward based on distance from origin
        val distance = sqrt(p.x.pow(2) + p.y.pow(2))
        val radialDeformation = p.x * radialEffect * distance
        // Random noise perturbation
        val noise = (Dice.randomDouble() - 0.5) * noiseFactor

        return p.x + sinDeformation + radialDeformation + noise
    }

    // Y-coordinate deformation
    private fun deformY(p: Point): Double {
        // Sinusoidal deformation based on x-coordinate
        val sinDeformation = amplitudeY * sin(frequencyY * p.x)
        // Radial deformation, pushing points outward based on distance from origin
        val distance = sqrt(p.x.pow(2) + p.y.pow(2))
        val radialDeformation = p.y * radialEffect * distance
        // Random noise perturbation
        val noise = (Dice.randomDouble() - 0.5) * noiseFactor

        return p.y + sinDeformation + radialDeformation + noise
    }
}