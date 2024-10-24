package evofun.bio

import evofun.bio.serialize.DNASerializer
import java.io.File
import kotlin.random.Random

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

    fun readRandomDNA(): DNA? {
        // List all JSON files in the dnaLocation directory
        val dnaFiles = dnaLocation.listFiles { file -> file.extension == "json" } ?: return null

        // Ensure there are files to read from
        if (dnaFiles.isEmpty()) {
            println("No DNA files found in the directory.")
            return null
        }

        // Select a random file from the list
        val randomFile = dnaFiles[Random.nextInt(dnaFiles.size)]
        println("Reading DNA from file: ${randomFile.name}")

        // Read the file content and deserialize it into a DNA object
        val dnaJsonString = randomFile.readText()
        return DNASerializer.deserialize(dnaJsonString)
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