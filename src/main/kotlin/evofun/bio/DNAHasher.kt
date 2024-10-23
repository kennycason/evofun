package evofun.bio

import evofun.bio.serialize.DNASerializer
import evofun.color.ColorFunction
import evofun.function2d.Function2D
import java.security.MessageDigest
import java.util.UUID
import kotlin.text.Charsets.UTF_8

object DNAHasher {
    fun sha256Hash(dna: DNA): String {
        val dnaJsonString = DNASerializer.serialize(dna)
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(dnaJsonString.toByteArray(UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }  // Convert bytes to hex string
    }

}

