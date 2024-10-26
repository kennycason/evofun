package evofun.bio

import evofun.color.ColorFunction
import evofun.color.CosmicPulseColorFunction
import evofun.color.DefaultColorFunction
import evofun.color.NeonColorFunction
import evofun.color.WeightedRGBColorFunction
import evofun.function2d.Abs
import evofun.function2d.Deformation
import evofun.function2d.Guassian
import evofun.function2d.Horseshoe
import evofun.function2d.Parabola
import evofun.function2d.Pdj
import evofun.function2d.Function2D
import evofun.function2d.MandelbrotLike
import evofun.function2d.Popcorn
import evofun.function2d.RadialBlur
import evofun.function2d.Rotate
import evofun.function2d.Scale
import evofun.function2d.SinCos
import evofun.function2d.SinSin
import evofun.function2d.Spherical
import evofun.function2d.Spiral
import evofun.function2d.Squared
import evofun.function2d.Swirl
import evofun.function2d.Translate
import evofun.random.Dice
import kotlin.math.max
import kotlin.math.min

object Genetic {

    fun buildDNA(length: Int): DNA {
        return DNA(
            genes = MutableList(size = length) { randomGene() },
            colorGene = buildRandomColorGene(),
            geneExpressionOrder = DNA.GeneExpressionOrder.SEQUENTIAL_ITERATIVE
        )
    }

    private fun buildRandomColorGene(): DNA.ColorGene {
        return DNA.ColorGene(
            algorithm = DNA.ColorGene.ColorAlgorithm.FUNCTIONS, // DNA.ColorGene.ColorAlgorithm.entries.random(),
            genes = MutableList(size = 3) { randomGene() },
            alpha = Dice.nextDouble()
        )
    }

    private fun randomGene() = DNA.Gene(
        function = DNA.GeneFunction.entries.random(),
        a = Dice.randomDouble(),
        b = Dice.randomDouble(),
        c = Dice.randomDouble(),
        d = Dice.randomDouble(),
        e = Dice.randomDouble(),
        f = Dice.randomDouble()
    )

    fun mutateDna(dna: DNA, probability: Double) {
        mutateColorGene(dna.colorGene, probability)
        for (gene in dna.genes) {
            mutateGene(gene, probability, min = -1.0, max = 1.0)
        }
        if (Dice.nextDouble() < probability / 2) {
         //   dna.geneExpressionOrder = DNA.GeneExpressionOrder.entries.random()
        }
    }

    private fun mutateGene(gene: DNA.Gene, probability: Double, min: Double = -10.0, max: Double = 10.0) {
        gene.function = if (Dice.nextDouble() < probability) {
            // println("mutate function")
            DNA.GeneFunction.entries.random()
        } else gene.function
        gene.a = mutateDouble(gene.a, probability, min = min, max = max)
        gene.b = mutateDouble(gene.b, probability, min = min, max = max)
        gene.c = mutateDouble(gene.c, probability, min = min, max = max)
        gene.d = mutateDouble(gene.d, probability, min = min, max = max)
        gene.e = mutateDouble(gene.e, probability, min = min, max = max)
        gene.f = mutateDouble(gene.f, probability, min = min, max = max)
    }

    private fun mutateColorGene(colorGene: DNA.ColorGene, probability: Double) {
        colorGene.algorithm = if (Dice.nextDouble() < probability) {
            println("mutate color algorithm")
            DNA.ColorGene.ColorAlgorithm.entries.random()
        } else colorGene.algorithm
        colorGene.genes.forEach {
            mutateGene(it, probability * 2, min = -10.0, max = 10.0)
        }
        colorGene.alpha = mutateDouble(colorGene.alpha, probability, min = 0.5, max = 1.0)
    }

    private fun mutateDouble(value: Double, probability: Double, min: Double = -1.0, max: Double = 1.0): Double {
        if (Dice.nextDouble() >= probability) return value
        val newValue = value + Dice.randomDouble() / 10
        return if (newValue < min) min
        else if (newValue > max) max
        else newValue
    }

    fun reproduce(dna1: DNA, dna2: DNA): DNA {
        val longestDna =  if (dna1.genes.size >= dna2.genes.size) dna1 else dna2
        val shortestDna =  if (dna1.genes.size < dna2.genes.size) dna1 else dna2

        val clone = clone(longestDna)
        for (i in 0 until shortestDna.genes.size) {
            if (Dice.nextBoolean()) {
                clone.genes[i] = shortestDna.genes[i]
            }
        }
        for (i in 0 until shortestDna.colorGene.genes.size) {
            if (Dice.nextBoolean()) {
                clone.colorGene.genes[i] = shortestDna.colorGene.genes[i]
            }
        }
        return clone
    }

    fun express(dna: DNA): ExpressedDNA {
        return ExpressedDNA(
            dna = dna,
            expressed = expressGenes(dna.genes),
            colorFunction = expressColorFunction(dna)
        )
    }

    fun expressGenes(genes: List<DNA.Gene>): List<Function2D> {
        return genes.map(::expressGene)
    }

    private fun expressGene(gene: DNA.Gene): Function2D {
        return when (gene.function) {
            DNA.GeneFunction.ABS -> Abs(a = gene.a, b = gene.b)
            DNA.GeneFunction.GUASSIAN -> Guassian(x0 = gene.a, y0 = gene.b, sigmaX = gene.c, sigmaY = gene.d, amplitude = gene.e)
            DNA.GeneFunction.HORSESHOE -> Horseshoe(a = gene.a, b = gene.b, c = gene.c, d = gene.d)
            DNA.GeneFunction.PARABOLA -> Parabola(a = gene.a, b = gene.b, c = gene.c, d = gene.d, e = gene.e, f = gene.f)
            DNA.GeneFunction.PDJ -> Pdj(a = gene.a, b = gene.b, c = gene.c, d = gene.d)
            DNA.GeneFunction.POPCORN -> Popcorn(a = gene.a, b = gene.b)
            DNA.GeneFunction.SIN_COS -> SinCos(a = gene.a, b = gene.b, c = gene.c, d = gene.d, e = gene.e, f = gene.f)
            DNA.GeneFunction.SIN_SIN -> SinSin(a = gene.a, b = gene.b, c = gene.c, d = gene.d, e = gene.e, f = gene.f)
            DNA.GeneFunction.SPHERICAL -> Spherical(a = gene.a, b = gene.b, c = gene.c, d = gene.d)
            DNA.GeneFunction.SPIRAL -> Spiral(a = gene.a, b = gene.b, c = gene.c, d = gene.d)
            DNA.GeneFunction.SQUARED -> Squared(a = gene.a, b = gene.b, c = gene.c, d = gene.d)
            DNA.GeneFunction.SWIRL -> Swirl(a = gene.a, b = gene.b, c = gene.c, d = gene.d)

//            DNA.GeneFunction.MANDELBROT_LIKE -> MandelbrotLike(a = gene.a, b = gene.b, c = gene.c, d = gene.d, e = gene.e, f = gene.f)
//            DNA.GeneFunction.RADIAL_BLUR -> RadialBlur(a = gene.a, b = gene.b, intensity = gene.c)

            DNA.GeneFunction.ROTATE -> Rotate(theta = gene.a, centerX = gene.b, centerY = gene.c)
            DNA.GeneFunction.SCALE -> Scale(scaleX = gene.a, scaleY = gene.b)
            DNA.GeneFunction.TRANSLATE -> Translate(dx = gene.a, dy = gene.b)
            DNA.GeneFunction.DEFORMATION -> Deformation(
                frequencyX = gene.a,
                frequencyY = gene.b,
                amplitudeX = gene.c,
                amplitudeY = gene.d,
                radialEffect = gene.e,
                noiseFactor = gene.f
            )
        }
    }

    private fun expressColorFunction(dna: DNA): ColorFunction {
        return when (dna.colorGene.algorithm) {
            DNA.ColorGene.ColorAlgorithm.DEFAULT -> DefaultColorFunction(
                f1 = dna.colorGene.genes.first().a,
                f2 = dna.colorGene.genes.first().b,
                f3 = dna.colorGene.genes.first().c,
                p1 = dna.colorGene.genes.first().d,
                p2 = dna.colorGene.genes.first().e,
                p3 = dna.colorGene.genes.first().f,
                center = 128,
                width = 127,
                alpha = dna.colorGene.alpha
            )
            DNA.ColorGene.ColorAlgorithm.FUNCTIONS -> WeightedRGBColorFunction(gene = dna.colorGene)
            DNA.ColorGene.ColorAlgorithm.NEON -> NeonColorFunction(
                f1 = dna.colorGene.genes.first().a,
                f2 = dna.colorGene.genes.first().b,
                f3 = dna.colorGene.genes.first().c,
                p1 = dna.colorGene.genes.first().d,
                p2 = dna.colorGene.genes.first().e,
                p3 = dna.colorGene.genes.first().f,
                intensity = dna.colorGene.genes[1].a * 5.0,
                alpha = dna.colorGene.alpha
            )
            DNA.ColorGene.ColorAlgorithm.COSMIC_PULSE -> CosmicPulseColorFunction(
                rFrequency = dna.colorGene.genes.first().a,
                gFrequency = dna.colorGene.genes.first().a,
                bFrequency = dna.colorGene.genes.first().a,
                rPhase = dna.colorGene.genes.first().a,
                gPhase = dna.colorGene.genes.first().a,
                bPhase = dna.colorGene.genes.first().a,
                brightness = dna.colorGene.genes[1].a * 2,
                alpha = dna.colorGene.alpha
            )
        }
    }

    fun clone(dna: DNA): DNA {
        return DNA(
            genes = dna.genes.map {
                DNA.Gene(
                    function = it.function,
                    a = it.a,
                    b = it.b,
                    c = it.c,
                    d = it.d,
                    e = it.e,
                    f = it.f
                )
            }.toMutableList(),
            colorGene = DNA.ColorGene(
                algorithm = dna.colorGene.algorithm,
                genes = dna.genes.map {
                    DNA.Gene(
                        function = it.function,
                        a = it.a,
                        b = it.b,
                        c = it.c,
                        d = it.d,
                        e = it.e,
                        f = it.f
                    )
                }.toMutableList(),
                alpha = dna.colorGene.alpha
            ),
            geneExpressionOrder = dna.geneExpressionOrder
        )
    }

}