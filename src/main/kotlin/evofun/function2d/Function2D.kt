package evofun.function2d

import evofun.geometry.Point
import kotlin.math.sqrt

interface Function2D {
    fun apply(p: Point): Point
    fun applyInPlace(p: Point)
    fun r(p: Point) = sqrt(p.x * p.x + p.y * p.y)
}