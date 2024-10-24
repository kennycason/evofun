package evofun.sound

import org.jtransforms.fft.FloatFFT_1D
import javax.sound.sampled.*
import kotlin.concurrent.thread

class LiveBeatDetector {

    private val bufferSize = 2048  // Buffer size (power of 2 for FFT)
    private val floatBuffer = FloatArray(bufferSize)
    private val byteBuffer = ByteArray(bufferSize * 2)  // 2 bytes per sample (16-bit PCM)
    private val fft = FloatFFT_1D(bufferSize.toLong())
    private val threshold = 0.9f  // Amplitude threshold for detecting beats

    fun startDetection(onDetect: () -> Unit) {
        // Define the audio format for capturing microphone input
        val format = AudioFormat(44100f, 16, 1, true, false)  // 44.1kHz, 16-bit, mono

        // Set up the target data line for microphone input
        val line = AudioSystem.getTargetDataLine(format)
        line.open(format, bufferSize * 2)
        line.start()

        println("Microphone open, starting beat detection...")

        // Start a thread to process microphone input
        thread {
            while (true) {
                val bytesRead = line.read(byteBuffer, 0, byteBuffer.size)

                if (bytesRead > 0) {
                    // Convert byte buffer to float buffer (16-bit PCM)
                    for (i in 0 until bufferSize) {
                        val low = byteBuffer[2 * i].toInt() and 0xff
                        val high = byteBuffer[2 * i + 1].toInt()
                        floatBuffer[i] = (high shl 8 or low).toFloat() / Short.MAX_VALUE
                    }

                    // Apply FFT
                    fft.realForward(floatBuffer)

                    // Detect beats based on amplitude in FFT results
                    val maxAmplitude = floatBuffer.maxOrNull() ?: 0f
                    if (maxAmplitude > threshold) {
                        val currentTime = System.currentTimeMillis() / 1000.0
                        println("Beat detected at time: $currentTime seconds with amplitude: $maxAmplitude")
                        onDetect()
                    }
                }
            }
        }
    }
}
