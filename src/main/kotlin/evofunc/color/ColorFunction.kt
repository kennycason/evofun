package evofunc.color

import evofunc.bio.Organism
import java.awt.Color

interface ColorFunction {
    fun apply(x: Int, y: Int, buffer: Array<Array<Organism.Cell>>, maxCount: Double): Color
}