package evofun.color

import evofun.bio.DNA
import evofun.bio.Genetic
import evofun.bio.Organism
import evofun.geometry.Point
import java.awt.Color
import java.util.Random
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

data class WeightedRGBColorFunction(
    val gene: DNA.ColorGene,
    var colorMixAlgorithm: ColorMixAlgorithm = ColorMixAlgorithm.entries.random()
) : ColorFunction {
    enum class ColorMixAlgorithm {
        MAX,
        SUM,
        AVG
    }

    private val colorFunctions = Genetic.expressGenes(gene.genes)
    private val r = colorFunctions[0]
    private val g = colorFunctions[1]
    private val b = colorFunctions[2]

    override fun apply(x: Int, y: Int, buffer: Array<Array<Organism.Cell>>, maxCount: Double): Color {
        if (buffer[x][y].count == 0.0) return Color.BLACK
        val p = Point(buffer[x][y].count, buffer[x][y].count)
        val rp = r.apply(p)
        val gp = g.apply(p)
        val bp = b.apply(p)
        return when (colorMixAlgorithm) {
            ColorMixAlgorithm.SUM -> Color(
                clamp(rp.x + rp.y).toFloat(),
                clamp(gp.x + gp.y).toFloat(),
                clamp(bp.x + bp.y).toFloat(),
                gene.alpha.toFloat()
            )
            ColorMixAlgorithm.MAX -> return Color(
                clamp(max(rp.x, rp.y)).toFloat(),
                clamp(max(gp.x, gp.y)).toFloat(),
                clamp(max(bp.x, bp.y)).toFloat(),
                gene.alpha.toFloat()
            )
            ColorMixAlgorithm.AVG -> return Color(
                clamp((rp.x + rp.y) / 2).toFloat(),
                clamp((gp.x + gp.y) / 2).toFloat(),
                clamp((bp.x + bp.y) / 2).toFloat(),
                gene.alpha.toFloat()
            )
        }
    }

    private fun clamp(v: Double): Double {
        return max(min(abs(v), 1.0), 0.0)
    }

}