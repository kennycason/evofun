package evofun.bio.serialize

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import evofun.bio.DNA

/**
 * DNA is JSON structured as
 */
object DNASerializer {
    private val objectMapper: ObjectMapper = ObjectMapper()
        .registerModule(KotlinModule())

    fun serialize(dna: DNA): String {
        return objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(dna)
    }

    fun deserialize(dna: String): DNA {
        return objectMapper.readValue(dna, DNA::class.java)
    }

}
