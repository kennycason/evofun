package evofunc.bio

import evofunc.color.Colorizer
import evofunc.function.PointFunction

data class DNA(
    val genes: List<Gene>,
    val colorizer: Colorizer
) {
    val size = genes.size
}

data class ExpressedDNA(
    val dna: DNA,
    val expressed: List<PointFunction>,
)

data class Gene(
    var function: GeneFunction,
    var a: Double, var b: Double, var c: Double, var d: Double, var e: Double, var f: Double
)

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
}
