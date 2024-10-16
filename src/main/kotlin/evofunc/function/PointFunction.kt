package evofunc.function

import evofunc.geometry.Point
import kotlin.math.sqrt

interface PointFunction {
    fun apply(p: Point): Point
    fun applyInPlace(p: Point)
    fun r(p: Point) = sqrt(p.x * p.x + p.y * p.y)
}