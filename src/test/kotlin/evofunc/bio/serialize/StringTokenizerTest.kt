package evofun.bio.serialize

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StringTokenizerTest {

    @Test // NOT an actual DNA sequence, just demonstrating how tokenizer works.
    fun `basic tokenizer test`() {
        val dna = "genes:SIN_COS,0.0,0.1,0.2,0.3,0.4,0.5|PARABOLA,0.0,0.1,0.2,0.3,0.4,0.5:colorGenes:SIN_COS,0.0,0.1,0.2,0.3,0.4,0.5:geneExpressionOrder:SEQUENTIAL_ITERATIVE"
        val tokenizer = StringTokenizer(dna, ":")
        assertTrue(tokenizer.hasNextToken())
        assertEquals("genes", tokenizer.nextToken().value)

        val genes = tokenizer.nextToken().value
        assertEquals("SIN_COS,0.0,0.1,0.2,0.3,0.4,0.5|PARABOLA,0.0,0.1,0.2,0.3,0.4,0.5", genes)
        val geneTokenizer = StringTokenizer(genes, "|")
        assertEquals("SIN_COS,0.0,0.1,0.2,0.3,0.4,0.5", geneTokenizer.nextToken().value)
        assertEquals("PARABOLA,0.0,0.1,0.2,0.3,0.4,0.5", geneTokenizer.nextToken().value)

        assertEquals("colorGenes", tokenizer.nextToken().value)
        assertEquals("SIN_COS,0.0,0.1,0.2,0.3,0.4,0.5", tokenizer.nextToken().value)
        assertEquals("geneExpressionOrder", tokenizer.nextToken().value)
        assertEquals("SEQUENTIAL_ITERATIVE", tokenizer.nextToken().value)
        assertFalse(tokenizer.hasNextToken())
    }

}