package evofun.image

import evofun.bio.Organism
import java.awt.image.BufferedImage
import kotlin.math.ln
import kotlin.math.log2

object Entropy {
    fun calculateNormalizedEntropy(image: BufferedImage): Double {
        val entropy = calculateEntropy(image)
        val totalPixels = image.width * image.height
        val maxEntropy = log2(totalPixels.toDouble())

        // normalize entropy to a 0.0 - 1.0 scale
        return entropy / maxEntropy
    }

    fun calculateEntropy(image: BufferedImage): Double {
        val colorCountMap = mutableMapOf<Int, Int>()
        val totalPixels = image.width * image.height

        // Count occurrences of each color
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                val rgb = image.getRGB(x, y)
                colorCountMap[rgb] = colorCountMap.getOrDefault(rgb, 0) + 1
            }
        }

        // calculate the entropy
        var entropy = 0.0
        for ((_, count) in colorCountMap) {
            val probability = count.toDouble() / totalPixels
            entropy -= probability * log2(probability)
        }
        return entropy
    }

    fun calculateColorHistogram(image: BufferedImage): Map<Int, Int> {
        val histogram = mutableMapOf<Int, Int>()
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                val color = image.getRGB(x, y)
                histogram[color] = histogram.getOrDefault(color, 0) + 1
            }
        }
        return histogram
    }

    fun calculateNormalizedEntropy(buffer: Array<Array<Organism.Cell>>): Double {
        val flattened = buffer.flatten()

        // compute histogram
        val valueCountMap = mutableMapOf<Int, Int>()
        for (value in flattened) {
            valueCountMap[value.count.toInt()] =
                valueCountMap.getOrDefault(value.count.toInt(), 0) + 1
        }

        val totalElements = flattened.size.toDouble()

        // calc current entropy
        var entropy = 0.0
        for ((_, count) in valueCountMap) {
            val probability = count / totalElements
            entropy -= probability * ln(probability) / ln(2.0) // log base 2
        }

        // maximum possible entropy, then normalize entropy between 0 and 1
        val maxEntropy = log2(totalElements)
        return entropy / maxEntropy
    }

}