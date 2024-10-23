package evofun.image

import evofun.bio.Organism
import evofun.util.array2d
import java.awt.image.BufferedImage

object ImageToGrayScaleBuffer {
    fun toBuffer(bi: BufferedImage, scale: Double = 1.0): Array<Array<Double>> {
        val buffer = array2d(bi.width, bi.height) { 0.0 }
        updateBuffer(bi, scale, buffer)
        return buffer
    }

    fun updateBuffer(bi: BufferedImage, scale: Double = 1.0, buffer: Array<Array<Double>>) {
        val scaleTerm = scale / (1000 * 0xFF)
        (0..< bi.width).forEach{ x ->
            (0..< bi.height).forEach{ y ->
                val rgb = bi.getRGB(x, y)
                val red = (rgb shr 16 and 0xFF) * 0x12B  // 0x12B = 299
                val green = (rgb shr 8 and 0xFF) * 0x24B // 0x24B = 587
                val blue = (rgb and 0xFF) * 0x72         // 0x72 = 114

                buffer[x][y] = scaleTerm * (red + green + blue)
            }
        }
    }

    fun addToBuffer(bi: BufferedImage, scale: Double = 1.0, buffer: Array<Array<Organism.Cell>>) {
        val scaleTerm = scale / (1000 * 0xFF)
        (0..< bi.width).forEach{ x ->
            (0..< bi.height).forEach{ y ->
                val rgb = bi.getRGB(x, y)
                val red = (rgb shr 16 and 0xFF) * 0x12B  // 0x12B = 299
                val green = (rgb shr 8 and 0xFF) * 0x24B // 0x24B = 587
                val blue = (rgb and 0xFF) * 0x72         // 0x72 = 114

                buffer[x][y].count += scaleTerm * (red + green + blue)
            }
        }
    }
}