package mmxxv

import io.kotest.matchers.shouldBe
import mmxxv.input.input

private fun List<String>.parse(): Pair<List<LongRange>, List<Long>> {
    val ranges = takeWhile { it != "" }
        .map { it.split("-") }
        .map { (start, end) ->
            LongRange(start = start.toLong(), endInclusive = end.toLong())
        }

    val ingredients = dropWhile { it != "" }
        .drop(1)
        .map { it.toLong() }

    return ranges to ingredients
}

private fun Day05Part1(input: List<String>): Int {
    val (ranges, ingredients) = input.parse()

    return ingredients
        .count { ing -> ranges.any { it.contains(ing) } }
}

private fun Day05Part2(input: List<String>): Long {
    val ranges = input.parse().first
        .sortedBy { it.last }.sortedBy { it.first }

    var count = 0L
    var merged = ranges.first()

    fun LongRange.size(): Long = 1 + (last - first)

    for (range in ranges.drop(1)) {
        when {
            range.last <= merged.last -> continue
            range.first > merged.last -> {
                count += merged.size()
                merged = range
            }

            range.first <= merged.last -> {
                merged = LongRange(merged.first, range.last)
            }
        }
    }

    return count + merged.size()
}

fun main() {
    val input = input(day = 5)

    Day05Part1(input.sample()) shouldBe 3
    Day05Part1(input.main()) shouldBe 737

    Day05Part2(input.sample()) shouldBe 16
    Day05Part2(input.main()) shouldBe 357485433193284L
}