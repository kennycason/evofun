package evofun.bio

import evofun.color.ColorFunction
import evofun.function2d.Function2D
import java.security.MessageDigest
import java.util.UUID
import kotlin.text.Charsets.UTF_8

data class DNA(
    val genes: MutableList<Gene>,
    val colorGene: ColorGene,
    var geneExpressionOrder: GeneExpressionOrder,
) {
    val size = genes.size

    data class Gene(
        var function: GeneFunction,
        var a: Double, var b: Double, var c: Double, var d: Double, var e: Double, var f: Double
    )

    enum class GeneExpressionOrder {
        RANDOM,
        SEQUENTIAL_ITERATIVE,
    }

    enum class GeneFunction {
        ABS,
        GUASSIAN,
        HORSESHOE,
        PARABOLA,
        PDJ,
        POPCORN,
        SIN_COS,
        SIN_SIN,
        SPHERICAL,
        SPIRAL,
        SQUARED,
        SWIRL,

//        MANDELBROT_LIKE,
//        RADIAL_BLUR,

        ROTATE,
        SCALE,
        TRANSLATE,
        DEFORMATION
    }

    data class ColorGene(
        var algorithm: ColorAlgorithm,
        var genes: MutableList<Gene>,
        var alpha: Double
    ) {
        enum class ColorAlgorithm {
            DEFAULT,
            FUNCTIONS,
            NEON,
            COSMIC_PULSE
        }
    }

}

data class ExpressedDNA(
    val dna: DNA,
    val expressed: List<Function2D>,
    val colorFunction: ColorFunction
)


