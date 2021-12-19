private val chunkDelimiters = listOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>'
)

private val syntaxErrorScore = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137
)

private val autocompleteScore = mapOf(
    ')' to 1,
    ']' to 2,
    '}' to 3,
    '>' to 4
)

private fun String.corruptedCharacterScore(): Int {
    val queue = mutableListOf<Char>()
    forEach { char ->
        // If the item is an opener, add it to the queue
        if (chunkDelimiters.map { it.first }.contains(char)) {
            queue.add(char)
        } else {
            // If the item is a closer, pop the nearest matching off
            val lastInQueue = queue.lastOrNull()

            // Make sure they're correct
            if (lastInQueue != null) {
                val (_, closer) = chunkDelimiters.find { it.first == lastInQueue }!!
                if (closer != char) {
                    return syntaxErrorScore[char]!!
                }
            }

            // pop the last one off
            queue.removeLastOrNull()
        }
    }

    return 0
}

fun Day10Part1(input: List<String>): Int = input.sumOf { it.corruptedCharacterScore() }


private fun String.completedSuffixScore(): Long {
    val queue = mutableListOf<Char>()
    forEach { char ->
        // If the item is an opener, add it to the queue
        if (chunkDelimiters.map { it.first }.contains(char)) {
            queue.add(char)
        } else {
            // Pop the last off, assuming corrupt rows have been filtered out
            queue.removeLastOrNull()
        }
    }

    return queue
        .reversed()
        .map { openingChar -> chunkDelimiters.find { it.first == openingChar }!!.second }
        .map { autocompleteScore[it]!!.toLong() }
        .reduce { acc, charScore -> (acc * 5) + charScore }
}

fun Day10Part2(input: List<String>): Long {
    val autocompleteScores = input
        .map { it.trim() }
        .filter { it.corruptedCharacterScore() == 0 }
        .map { it.completedSuffixScore() }
        .sorted()

    return autocompleteScores[autocompleteScores.lastIndex/2]
}