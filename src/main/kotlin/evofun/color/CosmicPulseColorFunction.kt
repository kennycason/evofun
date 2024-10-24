package evofun.color

import evofun.bio.Organism
import java.awt.Color
import kotlin.math.sin

data class CosmicPulseColorFunction(
    var rFrequency: Double = 0.5,   // frequency for Red channel pulse
    var gFrequency: Double = 0.4,   // frequency for Green channel pulse
    var bFrequency: Double = 0.6,   // frequency for Blue channel pulse
    var rPhase: Double = 0.0,       // phase shift for Red channel
    var gPhase: Double = 1.0,       // phase shift for Green channel
    var bPhase: Double = 2.0,       // phase shift for Blue channel
    var brightness: Double = 1.5,   // overall brightness boost for cosmic glow
    var alpha: Double = 1.0
) : ColorFunction {

    override fun apply(x: Int, y: Int, buffer: Array<Array<Organism.Cell>>, maxCount: Double): Color {
        if (buffer[x][y].count == 0.0) return Color.BLACK

        // use the sine wave to simulate a pulsing cosmic effect based on the count
        val i = buffer[x][y].count
        val r = (sin(rFrequency * i + rPhase) * 127 + 128) * brightness
        val g = (sin(gFrequency * i + gPhase) * 127 + 128) * brightness
        val b = (sin(bFrequency * i + bPhase) * 127 + 128) * brightness

        return Color(
            r.coerceIn(0.0, 255.0).toInt(),
            g.coerceIn(0.0, 255.0).toInt(),
            b.coerceIn(0.0, 255.0).toInt(),
            (alpha * 255).toInt()
        )
    }
}