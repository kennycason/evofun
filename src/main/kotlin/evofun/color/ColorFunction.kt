package evofun.color

import evofun.bio.Organism
import java.awt.Color

interface ColorFunction {
    fun apply(x: Int, y: Int, buffer: Array<Array<Organism.Cell>>, maxCount: Double): Color
}