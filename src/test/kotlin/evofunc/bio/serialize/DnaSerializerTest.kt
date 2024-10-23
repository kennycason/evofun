package evofun.bio.serialize

import evofun.bio.Genetic
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DnaSerializerTest {

    @Test
    fun `basic test`() {
        val dna = Genetic.buildDNA(5)
        val dnaString = DNASerializer.serialize(dna)
        val dna2 = DNASerializer.deserialize(dnaString)
        println(dnaString)
        assertEquals(dna, dna2)
    }

}