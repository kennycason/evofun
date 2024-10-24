package evofun.bio

import evofun.bio.serialize.DNASerializer
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.random.Random

class DNAFarm(
    private val rooLocation: File = File(System.getProperty("user.home"), "evofun")
) {
    private val dnaLocation = File(rooLocation, "dna")

    init {
        initIfNeeded()
    }

    fun writeDNA(dna: DNA, canvas: BufferedImage) {
        val dnaHash = DNAHasher.sha256Hash(dna)
        val jsonFileName = "$dnaHash.json"
        val dnaJsonString = DNASerializer.serialize(dna)
        val dnaFile = File(dnaLocation, jsonFileName)
        dnaFile.writeText(dnaJsonString)
        println("DNA saved to $dnaFile")

        val expressedDNAFile = File(dnaLocation, "$dnaHash.png")
        ImageIO.write(canvas, "png", expressedDNAFile)
        println("DNA image saved to $expressedDNAFile")
    }

    fun readRandomDNA(): DNA? {
        val dnaFiles = dnaLocation.listFiles { file -> file.extension == "json" } ?: return null

        if (dnaFiles.isEmpty()) {
            println("No DNA files found in the directory.")
            return null
        }

        val randomFile = dnaFiles[Random.nextInt(dnaFiles.size)]
        println("Reading DNA from file: ${randomFile.name}")

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