package mmxxv

import io.kotest.matchers.shouldBe
import mmxxv.input.input
import kotlin.collections.dropLast
import kotlin.text.lastIndex

private data class Problem(val numbers: List<Long>, val operation: Char) {
    fun solve(): Long {
        return when (operation) {
            '+' -> numbers.sum()
            '*' -> numbers.reduce { a, b -> a * b }
            else -> throw UnsupportedOperationException()
        }
    }
}

private fun List<String>.ops(): List<Char> = last()
    .trim()
    .replace("\\s+".toRegex(), ".")
    .split(".")
    .map { it.first() }

/**
 * Parse the whole thing backwards, relying on the caller to interpret each column into Longs.
 */
private fun List<String>.parse(mapColumn: (List<String>) -> List<Long>): List<Problem> {
    val problemColumn = mutableListOf<List<Long>>()
    val numberLines = dropLast(1)

    var endIdx = first().lastIndex
    var idx = endIdx

    while (idx >= 0) {
        if (numberLines.all { it[idx] == ' ' } || idx == 0) {
            val numbers = numberLines.map {
                if (idx == 0) it.take(endIdx + 1) else it.substring(idx + 1, endIdx + 1)
            }

            problemColumn.add(mapColumn(numbers))

            endIdx = idx - 1
        }
        idx--
    }

    val numbers = problemColumn.reversed()

    return ops()
        .mapIndexed { index, operation ->
            Problem(numbers[index], operation)
        }
}

private fun Day06Part1(input: List<String>): Long {
    return input.parse { numbers ->
        numbers.map { it.trim().toLong() }
    }.sumOf { problem -> problem.solve() }
}

private fun Day06Part2(input: List<String>): Long {
    return input.parse { numbers ->
        val parsedNums = mutableListOf<Long>()
        var localIdx = numbers.maxOf { it.lastIndex }
        while (localIdx >= 0) {
            val num = numbers.map { it[localIdx] }
                .joinToString(separator = "")
                .trim()
                .toLong()

            parsedNums.add(num)

            localIdx--
        }

        parsedNums
    }.sumOf { problem -> problem.solve() }
}

fun main() {
    val input = input(day = 6)

    Day06Part1(input.sample()) shouldBe 4277556
    Day06Part1(input.main()) shouldBe 3968933219902

    Day06Part2(input.sample()) shouldBe 3263827
    Day06Part2(input.main()) shouldBe 6019576291014L
}