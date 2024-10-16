package evofunc.genetic

import evofunc.color.Colorizer
import evofunc.function.Abs
import evofunc.function.Horseshoe
import evofunc.function.Pdj
import evofunc.function.PointFunction
import evofunc.function.Popcorn
import evofunc.function.SinSin
import evofunc.function.Spherical
import evofunc.function.Spiral
import evofunc.function.Swirl
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
        gene.a = mutateDouble(gene.a, probability)
        gene.b = mutateDouble(gene.b, probability)
        gene.c = mutateDouble(gene.c, probability)
        gene.d = mutateDouble(gene.d, probability)
        gene.e = mutateDouble(gene.e, probability)
        gene.f = mutateDouble(gene.f, probability)
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

    private fun mutateDouble(value: Double, probability: Double): Double {
        if (Dice.nextDouble() >= probability) return value

        // println("mutate parameter")
        val newValue = if (Dice.nextBoolean()) value + Dice.nextDouble() / 4
        else value - Dice.nextDouble() / 4
        return if (newValue < -1) -1.0
        else if (newValue > 1) 1.0
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
            GeneFunction.SIN_SIN -> SinSin(a = gene.a, b = gene.b, c = gene.c, d = gene.d, e = gene.e, f = gene.f)
            GeneFunction.SPHERICAL -> Spherical(a = gene.a, b = gene.b, c = gene.c, d = gene.d)
            GeneFunction.SWIRL -> Swirl(a = gene.a, b = gene.b, c = gene.c, d = gene.d)
            GeneFunction.HORSESHOE -> Horseshoe(a = gene.a, b = gene.b, c = gene.c, d = gene.d)
            GeneFunction.POPCORN -> Popcorn(a = gene.a, b = gene.b)
            GeneFunction.PDJ -> SinSin(a = gene.a, b = gene.b, c = gene.c, d = gene.d, e = gene.e, f = gene.f)
            GeneFunction.ABS -> Abs(a = gene.a, b = gene.b)
            GeneFunction.SPIRAL -> Spiral(a = gene.a, b = gene.b, c = gene.c, d = gene.d)
        }
    }

    private fun getRandomFunction(): GeneFunction {
        return GeneFunction.entries.toTypedArray().random()
    }

}