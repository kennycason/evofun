package evofun.image

import java.awt.image.BufferedImage
import kotlin.math.abs

/**
 * Created by kenny on 5/23/16.
 */
class ImageDifference(val stepSize: Int) {
    fun compare(bi: BufferedImage, bi2: BufferedImage): Double {
        var error = 0.0
        (0..< bi.width step stepSize).forEach{ x ->
            (0..< bi.height step stepSize).forEach{ y ->
                val rgb = bi.getRGB(x, y)
                val rgb2 = bi2.getRGB(x, y)
                error += abs((rgb and 0xFF) - (rgb2 and 0xFF))
                error += abs(((rgb shr 8) and 0xFF) - ((rgb2 shr 8) and 0xFF))
                error += abs(((rgb shr 16) and 0xFF) - ((rgb2 shr 16) and 0xFF))
            }
        }
        return error
    }
}