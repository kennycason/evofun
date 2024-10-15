package evofunc.genetic

import evofunc.function.PointFunction

data class Gene(
    var function: PointFunction,
    var a: Double, var b: Double, var c: Double, var d: Double, var e: Double, var f: Double
)
