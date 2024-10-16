package evofunc.genetic

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
    SIN_SIN,
    SPHERICAL,
    SWIRL,
    HORSESHOE,
    POPCORN,
    PDJ,
    ABS,
    SPIRAL
}
