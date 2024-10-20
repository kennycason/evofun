package evofunc.bio

import evofunc.color.ColorFunction
import evofunc.function.Abs
import evofunc.function.Deformation
import evofunc.function.Guassian
import evofunc.function.Horseshoe
import evofunc.function.Parabola
import evofunc.function.Pdj
import evofunc.function.PointFunction
import evofunc.function.Popcorn
import evofunc.function.Rotate
import evofunc.function.Scale
import evofunc.function.SinCos
import evofunc.function.SinSin
import evofunc.function.Spherical
import evofunc.function.Spiral
import evofunc.function.Squared
import evofunc.function.Swirl
import evofunc.function.Translate
import evofunc.random.Dice
import kotlin.math.max


object Genetic {

    fun buildDNA(length: Int): DNA {
        return DNA(
            genes = List(size = length) { randomGene() },
            colorFunction = ColorFunction.buildRandomColorizer(),
            geneExpressionOrder = GeneExpressionOrder.SEQUENTIAL_ITERATIVE
        )
    }

    private fun randomGene() = Gene(
        function = GeneFunction.entries.random(),
        a = Dice.randomDouble(),
        b = Dice.randomDouble(),
        c = Dice.randomDouble(),
        d = Dice.randomDouble(),
        e = Dice.randomDouble(),
        f = Dice.randomDouble()
    )

    fun mutateDna(dna: DNA, probability: Double) {
        mutateColorizer(dna.colorFunction, probability)
        for (gene in dna.genes) {
            mutateGene(gene, probability)
        }
        if (Dice.nextDouble() < probability / 2) {
            dna.geneExpressionOrder = GeneExpressionOrder.entries.random()
        }
    }

    private fun mutateGene(gene: Gene, probability: Double) {
        gene.function = if (Dice.nextDouble() < probability / 2) {
            // println("mutate function")
            GeneFunction.entries.random()
        } else gene.function
        gene.a = mutateDouble(gene.a, probability, min = -10.0, max = 10.0)
        gene.b = mutateDouble(gene.b, probability, min = -10.0, max = 10.0)
        gene.c = mutateDouble(gene.c, probability, min = -10.0, max = 10.0)
        gene.d = mutateDouble(gene.d, probability, min = -10.0, max = 10.0)
        gene.e = mutateDouble(gene.e, probability, min = -10.0, max = 10.0)
        gene.f = mutateDouble(gene.f, probability, min = -10.0, max = 10.0)
    }

    private fun mutateColorizer(colorFunction: ColorFunction, probability: Double) {
        colorFunction.function = if (Dice.nextDouble() < probability / 2) {
            println("mutate color function")
            ColorFunction.ColorFunction.entries.random()
        } else colorFunction.function
        colorFunction.f1 = mutateDouble(colorFunction.f1, probability)
        colorFunction.f2 = mutateDouble(colorFunction.f2, probability)
        colorFunction.f3 = mutateDouble(colorFunction.f3, probability)
        colorFunction.p1 = mutateDouble(colorFunction.p1, probability)
        colorFunction.p2 = mutateDouble(colorFunction.p2, probability)
        colorFunction.p3 = mutateDouble(colorFunction.p3, probability)
        colorFunction.alpha = max(mutateDouble(colorFunction.alpha, probability), 0.5)
    }

    private fun mutateDouble(value: Double, probability: Double, min: Double = -1.0, max: Double = 1.0): Double {
        if (Dice.nextDouble() >= probability) return value

        val newValue = value + Dice.randomDouble() / 10

        return if (newValue < min) min
        else if (newValue > max) max
        else newValue
    }

    fun express(dna: DNA): ExpressedDNA {
        return ExpressedDNA(
            dna = dna,
            expressed = dna.genes.map(::expressGene)
        )
    }

    private fun expressGene(gene: Gene): PointFunction {
        return when (gene.function) {
            GeneFunction.ABS -> Abs(a = gene.a, b = gene.b)
            GeneFunction.GUASSIAN -> Guassian(x0 = gene.a, y0 = gene.b, sigmaX = gene.c, sigmaY = gene.d, amplitude = gene.e)
            GeneFunction.HORSESHOE -> Horseshoe(a = gene.a, b = gene.b, c = gene.c, d = gene.d)
            GeneFunction.PARABOLA -> Parabola(a = gene.a, b = gene.b, c = gene.c, d = gene.d, e = gene.e, f = gene.f)
            GeneFunction.PDJ -> Pdj(a = gene.a, b = gene.b, c = gene.c, d = gene.d)
            GeneFunction.POPCORN -> Popcorn(a = gene.a, b = gene.b)
            GeneFunction.SIN_COS -> SinCos(a = gene.a, b = gene.b, c = gene.c, d = gene.d, e = gene.e, f = gene.f)
            GeneFunction.SIN_SIN -> SinSin(a = gene.a, b = gene.b, c = gene.c, d = gene.d, e = gene.e, f = gene.f)
            GeneFunction.SPHERICAL -> Spherical(a = gene.a, b = gene.b, c = gene.c, d = gene.d)
            GeneFunction.SPIRAL -> Spiral(a = gene.a, b = gene.b, c = gene.c, d = gene.d)
            GeneFunction.SQUARED -> Squared(a = gene.a, b = gene.b, c = gene.c, d = gene.d)
            GeneFunction.SWIRL -> Swirl(a = gene.a, b = gene.b, c = gene.c, d = gene.d)

            GeneFunction.ROTATE -> Rotate(theta = gene.a, centerX = gene.b, centerY = gene.c)
            GeneFunction.SCALE -> Scale(scaleX = gene.a, scaleY = gene.b)
            GeneFunction.TRANSLATE -> Translate(dx = gene.a, dy = gene.b)
            GeneFunction.DEFORMATION -> Deformation(
                frequencyX = gene.a,
                frequencyY = gene.b,
                amplitudeX = gene.c,
                amplitudeY = gene.d,
                radialEffect = gene.e,
                noiseFactor = gene.f
            )
        }
    }

    fun clone(dna: DNA): DNA {
        return DNA(
            genes = dna.genes.map {
                Gene(
                    function = it.function,
                    a = it.a,
                    b = it.b,
                    c = it.c,
                    d = it.d,
                    e = it.e,
                    f = it.f
                )
            },
            colorFunction = ColorFunction(
                function = dna.colorFunction.function,
                f1 = dna.colorFunction.f1,
                f2 = dna.colorFunction.f2,
                f3 = dna.colorFunction.f3,
                p1 = dna.colorFunction.p1,
                p2 = dna.colorFunction.p2,
                p3 = dna.colorFunction.p3,
                center = dna.colorFunction.center,
                width = dna.colorFunction.width,
                alpha = dna.colorFunction.alpha
            ),
            geneExpressionOrder = dna.geneExpressionOrder
        )
    }

}