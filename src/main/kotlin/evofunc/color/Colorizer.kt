package evofunc.color

import java.awt.Color
import java.util.Random
import kotlin.math.sin

data class Colorizer(
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

    fun apply(i: Double): Color {
        val r = sin(f1 * i + p1) * width + center
        val g = sin(f2 * i + p2) * width + center
        val b = sin(f3 * i + p3) * width + center
//        if ((r > 0xFF || r < 0) || (g > 0xFF || g < 0) || (b > 0xFF || b < 0))
//            println("problem")
        return Color(r.toInt(), g.toInt(), b.toInt(), (alpha * 0xFF).toInt())
    }

    companion object {
        private val random = Random()
        fun buildRandomColorizer() = Colorizer(
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