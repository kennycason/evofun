package evofunc.genetic

import evofunc.color.Colorizer

class DNA(
    val genes: List<Gene>,
    val colorizer: Colorizer
) {
    val size = genes.size
}