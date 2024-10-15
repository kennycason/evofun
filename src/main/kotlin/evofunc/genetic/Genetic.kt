package evofunc.genetic

import evofunc.color.Colorizer
import evofunc.function.Horseshoe
import evofunc.function.Pdj
import evofunc.function.PointFunction
import evofunc.function.Popcorn
import evofunc.function.SinSin
import evofunc.function.Spherical
import evofunc.function.Swirl
import evofunc.random.Dice
import java.util.Random


object Genetic {
    private const val allFunctionsCount = 6

    fun buildDNA(length: Int): DNA {
        return DNA(
            genes = List(size = length) { randomGene() },
            colorizer = Colorizer.buildRandomColorizer()
        )
    }

    private fun randomGene() = Gene(
        function = buildRandomFunction(),
        a = Dice.randomDouble(),
        b = Dice.randomDouble(),
        c = Dice.randomDouble(),
        d = Dice.randomDouble(),
        e = Dice.randomDouble(),
        f = Dice.randomDouble()
    )

    fun mutateDna(dna: DNA, probability: Double) {
        if (Dice.nextInt(10) <= 1) {
           mutateColorizer(dna.colorizer)
            return
        }
        for (gene in dna.genes) {
            mutateGene(gene, probability)
        }
    }

    private fun mutateGene(gene: Gene, probability: Double) {
        if (Dice.randomDouble() >= probability) return

        val mutateSelection = Dice.nextInt(10)
        when (mutateSelection) {
            0 -> gene.function = buildRandomFunction()
            else -> {
                val i = Dice.nextInt(6) // 6 total parameters per gene
                when (i) {
                    0 -> gene.a = Dice.randomDouble()
                    1 -> gene.b = Dice.randomDouble()
                    2 -> gene.c = Dice.randomDouble()
                    3 -> gene.d = Dice.randomDouble()
                    4 -> gene.e = Dice.randomDouble()
                    5 -> gene.f = Dice.randomDouble()
                }
            }
        }
    }

    private fun mutateColorizer(colorizer: Colorizer) {
        val i = Dice.nextInt(6)
        when (i) {
            0 -> colorizer.f1 = Dice.randomDouble()
            1 -> colorizer.f2 = Dice.randomDouble()
            2 -> colorizer.f3 = Dice.randomDouble()
            3 -> colorizer.p1 = Dice.randomDouble()
            4 -> colorizer.p2 = Dice.randomDouble() * 2
            5 -> colorizer.p3 = Dice.randomDouble() * 4
//            6 -> colorizer.center = random.nextInt(128)
//            7 -> colorizer.width = random.nextInt(127)
        }
    }

    private fun buildRandomFunction(): PointFunction {
        val i = Dice.nextInt(allFunctionsCount)
        return when (i) {
            0 -> SinSin()
            1 -> Spherical()
            2 -> Swirl()
            3 -> Horseshoe()
            4 -> Popcorn()
            5 -> Pdj()
            else -> throw IllegalStateException()
        }
    }
}