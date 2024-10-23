package evofun.bio

import evofun.bio.serialize.DNASerializer
import java.io.File

class DNAFarm(
    private val rooLocation: File = File(System.getProperty("user.home"), ".evofun")
) {
    private val dnaLocation = File(rooLocation, "dna")

    init {
        initIfNeeded()
    }

    fun writeDNA(dna: DNA) {
        val dnaJsonString = DNASerializer.serialize(dna)
        val fileName = "${DNAHasher.sha256Hash(dna)}.json"
        val dnaFile = File(dnaLocation, fileName)
        dnaFile.writeText(dnaJsonString)
        println("DNA saved to $dnaFile")
    }

    private fun initIfNeeded() {
        if (!rooLocation.exists()) {
            rooLocation.mkdirs()
        }
        if (!dnaLocation.exists()) {
            dnaLocation.mkdirs()
        }
    }
}