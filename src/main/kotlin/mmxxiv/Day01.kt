@file:Suppress("FunctionName")

package mmxxiv

import io.kotest.matchers.shouldBe
import mmxxiv.input.input
import kotlin.math.abs

private fun splitLists(lines: List<String>): Pair<List<Int>, List<Int>> {
    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()

    lines.forEach { line ->
        val first = line.takeWhile { !it.isWhitespace() }.toInt()
        val second = line.takeLastWhile { !it.isWhitespace() }.toInt()

        left.add(first)
        right.add(second)
    }

    return left to right
}

private fun Day1Part1(lines: List<String>): Int {
    val (left, right) = splitLists(lines)

    return left.sorted()
        .zip(right.sorted())
        .sumOf { (first, second) -> abs(first - second) }
}

private fun Day1Part2(lines: List<String>): Int {
    val (left, right) = splitLists(lines)

    // Count occurrences of each item in the right list
    val occurrences = mutableMapOf<Int, Int>()

    right.forEach { rightValue ->
        occurrences.compute(rightValue) { _, count -> (count ?: 0) + 1 }
    }

    // Multiply each left value by the number of times it appears in the right list
    return left.sumOf { leftValue ->
        leftValue * (occurrences[leftValue] ?: 0)
    }
}

fun main() {
    val input = input(day = 1)

    Day1Part1(input.sample()) shouldBe 11
    Day1Part1(input.main()) shouldBe 2285373

    Day1Part2(input.sample()) shouldBe 31
    Day1Part2(input.main()) shouldBe 21142653
}