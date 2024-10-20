package evofunc.color

import evofunc.bio.Organism
import java.awt.Color
import java.util.Random
import kotlin.math.sin

data class ColorFunction(
    var function: ColorFunction = ColorFunction.DEFAULT,
    var f1: Double = 0.3,
    var f2: Double = 0.3,
    var f3: Double = 0.3,
    var p1: Double = 0.0,
    var p2: Double = 2.0,
    var p3: Double = 4.0,
    var center: Int = 128,
    var width: Int = 127,
    var alpha: Double = 1.0
) {
    enum class ColorFunction {
        DEFAULT,
        GRADIENT,
        NEIGHBOR_AVG,
        NEIGHBOR_AVG_AND_DEFAULT
    }

    fun apply(x: Int, y: Int, buffer: Array<Array<Organism.Cell>>, maxCount: Double): Color {
        if (buffer[x][y].count == 0.0) return Color.BLACK
        return when (function) {
            ColorFunction.DEFAULT -> applyDefault(buffer[x][y].count)
            ColorFunction.GRADIENT -> gradientColor(buffer[x][y].count, maxI = maxCount)
            ColorFunction.NEIGHBOR_AVG -> applyNeighborAverage(x, y, buffer)
            ColorFunction.NEIGHBOR_AVG_AND_DEFAULT -> {
                val c1 = applyDefault(buffer[x][y].count)
                val c2 = gradientColor(buffer[x][y].count, maxI = maxCount)
                Color(
                    (c1.red + c2.red) / 2, (c1.green + c2.green) / 2,
                    (c1.blue + c2.blue) / 2, (c1.alpha + c2.alpha) / 2
                )
            }
        }
    }

    private fun applyNeighborAverage(x: Int, y: Int, buffer: Array<Array<Organism.Cell>>): Color {
        val i = buffer[x][y].count
        val neighbors = listOf(
            buffer.getOrNull(x - 1)?.getOrNull(y), buffer.getOrNull(x + 1)?.getOrNull(y),
            buffer.getOrNull(x)?.getOrNull(y - 1), buffer.getOrNull(x)?.getOrNull(y + 1)
        ).filterNotNull()

        val avgCount = neighbors.map { it.count }.average()

        val r = (avgCount * f1 * i).coerceIn(0.0, 255.0).toInt()
        val g = (avgCount * f2 * i).coerceIn(0.0, 255.0).toInt()
        val b = (avgCount * f3 * i).coerceIn(0.0, 255.0).toInt()

        return Color(r, g, b, (alpha * 255).toInt())
    }

    private fun gradientColor(i: Double, maxI: Double): Color {
        val ratio = i / maxI
        val r = (255 * ratio).toInt()
        val g = (255 * (1 - ratio)).toInt()
        return Color(r, g, (128 + 127 * sin(i * 0.1)).toInt(), (alpha * 255).toInt())
    }

    private fun applyDefault(i: Double): Color {
        val r = sin(f1 * i + p1) * width + center
        val g = sin(f2 * i + p2) * width + center
        val b = sin(f3 * i + p3) * width + center
//        if ((r > 0xFF || r < 0) || (g > 0xFF || g < 0) || (b > 0xFF || b < 0))
//            println("problem")
        return Color(r.toInt(), g.toInt(), b.toInt(), (alpha * 0xFF).toInt())
    }

    companion object {
        private val random = Random()
        fun buildRandomColorizer() = ColorFunction(
            f1 = random.nextDouble(),
            f2 = random.nextDouble(),
            f3 = random.nextDouble(),
            p1 = random.nextDouble(),
            p2 = random.nextDouble() * 2,
            p3 = random.nextDouble() * 4,
            center = 128,
            width = 127
        )
    }
}