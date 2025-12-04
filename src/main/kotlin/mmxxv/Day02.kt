package mmxxv

import io.kotest.matchers.shouldBe
import mmxxv.input.input
import kotlin.collections.first

private fun List<String>.toRanges() = first().split(',')
    .map { range -> range.split('-') }
    .map { LongRange(it[0].toLong(), it[1].toLong()) }

private fun Day02Part1(input: List<String>): Long {
    val ranges = input.toRanges()

    fun Long.isInvalid(): Boolean {
        val str = toString()

        return when {
            str.length.mod(2) != 0 -> false

            str.substring(str.length / 2) == str.take(str.length / 2) -> true

            else -> false
        }
    }

    return ranges
        .flatten()
        .filter(Long::isInvalid)
        .sum()
}

private fun Day02Part2(input: List<String>): Long {
    val ranges = input.toRanges()

    fun Long.isInvalid(): Boolean {
        val str = toString()

        for (windowSize in 1..(str.length / 2)) {
            val candidate = buildString {
                repeat(str.length / windowSize) {
                    append(str.take(windowSize))
                }
            }
            if (str == candidate) {
                return true
            }
        }
        return false
    }

    return ranges
        .flatten()
        .filter(Long::isInvalid)
        .sum()
}

fun main() {
    val input = input(day = 2)

    Day02Part1(input.sample()) shouldBe 1227775554
    Day02Part1(input.main()) shouldBe 44487518055

    Day02Part2(input.sample()) shouldBe 4174379265
    Day02Part2(input.main()) shouldBe 53481866137
}