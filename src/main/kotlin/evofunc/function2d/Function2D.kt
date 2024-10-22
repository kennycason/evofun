package evofunc.function2d

import evofunc.geometry.Point
import kotlin.math.sqrt

interface Function2D {
    fun apply(p: Point): Point
    fun applyInPlace(p: Point)
    fun r(p: Point) = sqrt(p.x * p.x + p.y * p.y)
}