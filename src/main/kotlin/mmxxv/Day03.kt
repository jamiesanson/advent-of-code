package mmxxv

import io.kotest.matchers.shouldBe
import mmxxv.input.input

private fun String.nextBiggest(targetLength: Int): Int {
    return dropLast(targetLength - 1)
        .map { it.digitToInt() }
        .max()
}

private fun String.joltage(targetLength: Int): Long {
    var candidate = this

    return buildString {
        while (length < targetLength) {
            val next = candidate.nextBiggest(targetLength = targetLength - length)
            candidate = candidate.substringAfter(next.digitToChar())

            append(next)
        }
    }.toLong()
}

private fun Day03Part1(input: List<String>): Long {
    return input
        .sumOf { line -> line.joltage(targetLength = 2) }
}

private fun Day03Part2(input: List<String>): Long {
    return input
        .sumOf { line -> line.joltage(targetLength = 12) }
}

fun main() {
    val input = input(day = 3)

    Day03Part1(input.sample()) shouldBe 357
    Day03Part1(input.main()) shouldBe 17085

    Day03Part2(input.sample()) shouldBe 3121910778619
    Day03Part2(input.main()) shouldBe 169408143086082
}