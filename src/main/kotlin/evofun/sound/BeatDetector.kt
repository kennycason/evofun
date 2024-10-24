package evofun.sound

import org.jtransforms.fft.FloatFFT_1D
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

class BeatDetector(
    private val audioFile: File = File("./src/main/resources/Techno Turdle.wav")
) {

    private val beatTimes = mutableListOf<Double>()  // Store beat times

    fun startDetection(onDetect: () -> Unit) {
        val audioInputStream: AudioInputStream = AudioSystem.getAudioInputStream(audioFile)
        val format = audioInputStream.format
        val sampleRate = format.sampleRate.toInt()
        val bufferSize = 2048  // Power of 2
        val byteBuffer = ByteArray(bufferSize * 2)  // 2 bytes per sample (16-bit PCM)
        val floatBuffer = FloatArray(bufferSize)    // Float array for FFT
        val fft = FloatFFT_1D(bufferSize.toLong())

        var samplesProcessed = 0  // To track the number of samples processed
        val samplesPerSecond = sampleRate
        var bytesRead: Int

        println("Start processing")

        while (audioInputStream.read(byteBuffer) != -1) {
            // Convert byte buffer to float buffer (assume 16-bit PCM)
            for (i in 0 until bufferSize) {
                val low = byteBuffer[2 * i].toInt() and 0xff
                val high = byteBuffer[2 * i + 1].toInt()
                floatBuffer[i] = (high shl 8 or low).toFloat() / Short.MAX_VALUE
            }

            // Apply FFT
            fft.realForward(floatBuffer)

            // Detect beats based on amplitude in FFT results
            val maxAmplitude = floatBuffer.maxOrNull() ?: 0f
            if (maxAmplitude > 0.9) {
                // Calculate the time in seconds based on samples processed
                val timeInSeconds = samplesProcessed.toDouble() / samplesPerSecond
                println("Beat detected at time: $timeInSeconds seconds, amplitude: $maxAmplitude")
                beatTimes.add(timeInSeconds)  // Store beat time
            }

            // Update the total number of samples processed
            samplesProcessed += bufferSize
        }

        println("Processing finished. Total beats detected: ${beatTimes.size}")

        // Call the scheduleBeats function to map beat times to onChange function calls
        scheduleBeats(onDetect)
    }

    private fun scheduleBeats(onDetect: () -> Unit) {
        // Create a scheduled executor to trigger beat callbacks
        val executor = Executors.newSingleThreadScheduledExecutor()
        val startTime = System.currentTimeMillis()

        for (beatTime in beatTimes) {
            val delay = ((beatTime * 1000.0) - (System.currentTimeMillis() - startTime)).toLong()
            if (delay > 0) {
                executor.schedule({
                    onDetect()  // Call the onChange function for the beat
                }, delay, TimeUnit.MILLISECONDS)
            }
        }
    }

}