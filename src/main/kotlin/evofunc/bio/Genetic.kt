package evofunc.bio

import evofunc.color.Colorizer
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
            colorizer = Colorizer.buildRandomColorizer()
        )
    }

    private fun randomGene() = Gene(
        function = getRandomFunction(),
        a = Dice.randomDouble(),
        b = Dice.randomDouble(),
        c = Dice.randomDouble(),
        d = Dice.randomDouble(),
        e = Dice.randomDouble(),
        f = Dice.randomDouble()
    )

    fun mutateDna(dna: DNA, probability: Double) {
        mutateColorizer(dna.colorizer, probability)
        for (gene in dna.genes) {
            mutateGene(gene, probability)
        }
    }

    private fun mutateGene(gene: Gene, probability: Double) {
        gene.function = if (Dice.nextDouble() < probability / 2) {
            // println("mutate function")
            getRandomFunction()
        } else gene.function
        gene.a = mutateDouble(gene.a, probability, min = -10.0, max = 10.0)
        gene.b = mutateDouble(gene.b, probability, min = -10.0, max = 10.0)
        gene.c = mutateDouble(gene.c, probability, min = -10.0, max = 10.0)
        gene.d = mutateDouble(gene.d, probability, min = -10.0, max = 10.0)
        gene.e = mutateDouble(gene.e, probability, min = -10.0, max = 10.0)
        gene.f = mutateDouble(gene.f, probability, min = -10.0, max = 10.0)
    }

    private fun mutateColorizer(colorizer: Colorizer, probability: Double) {
        colorizer.f1 = mutateDouble(colorizer.f1, probability)
        colorizer.f2 = mutateDouble(colorizer.f2, probability)
        colorizer.f3 = mutateDouble(colorizer.f3, probability)
        colorizer.p1 = mutateDouble(colorizer.p1, probability)
        colorizer.p2 = mutateDouble(colorizer.p2, probability)
        colorizer.p3 = mutateDouble(colorizer.p3, probability)
        colorizer.alpha = max(mutateDouble(colorizer.alpha, probability), 0.5)
    }

    private fun mutateDouble(value: Double, probability: Double, min: Double = -1.0, max: Double = 1.0): Double {
        if (Dice.nextDouble() >= probability) return value

        // println("mutate parameter")
        val newValue =  value + Dice.randomDouble() / 10

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
            GeneFunction.DEFORMATION -> Deformation(frequencyX = gene.a, frequencyY = gene.b, amplitudeX = gene.c, amplitudeY = gene.d, radialEffect = gene.e, noiseFactor = gene.f)
        }
    }

    private fun getRandomFunction(): GeneFunction {
        return GeneFunction.entries.toTypedArray().random()
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
            colorizer = Colorizer(
                f1 = dna.colorizer.f1,
                f2 = dna.colorizer.f2,
                f3 = dna.colorizer.f3,
                p1 = dna.colorizer.p1,
                p2 = dna.colorizer.p2,
                p3 = dna.colorizer.p3,
                center = dna.colorizer.center,
                width = dna.colorizer.width,
                alpha = dna.colorizer.alpha,
            )
        )
    }

}