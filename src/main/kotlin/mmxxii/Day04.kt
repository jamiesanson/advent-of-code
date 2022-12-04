package mmxxii

private fun List<String>.ranges(): List<Pair<IntRange, IntRange>> {
    return map {
        val (first, second) = it.split(",")
        fun String.makeRange(): IntRange {
            val (start, end) = split('-')
            return IntRange(start.toInt(), end.toInt())
        }

        first.makeRange() to second.makeRange()
    }
}

fun Day04Part1(input: List<String>): Int = input
    .ranges()
    .filter { (first, second) ->
        first.all { second.contains(it) } || second.all { first.contains(it) }
    }.size

fun Day04Part2(input: List<String>): Int = input
    .ranges()
    .filter { (first, second) ->
        first.any { second.contains(it) }
    }.size