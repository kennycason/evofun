package evofun.bio.serialize

class StringTokenizer(
    private val sequence: String,
    private val delimiter: String
) {
    private var currentIndex = 0
    private var tokenIndex = 0

    fun hasNextToken(): Boolean {
        return currentIndex < sequence.length
    }

    fun nextToken(): Token {
        if (!hasNextToken()) {
            throw NoSuchElementException("No more tokens available")
        }
        val nextDelimiterIndex = sequence.indexOf(string = delimiter, startIndex = currentIndex)
        // If no more text, return the rest of the string as the final token
        val tokenValue = if (nextDelimiterIndex == -1) {
            val token = sequence.substring(currentIndex)
            currentIndex = sequence.length
            token
        } else { // read token
            val token = sequence.substring(currentIndex, nextDelimiterIndex)
            currentIndex = nextDelimiterIndex + delimiter.length
            token
        }

        return Token(tokenIndex++, tokenValue)
    }

    data class Token(
        val index: Int,
        val value: String
    )
}