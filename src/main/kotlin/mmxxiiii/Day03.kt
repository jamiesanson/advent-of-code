package mmxxiiii

import io.kotest.matchers.shouldBe
import mmxxiiii.input.input

private val mul = "mul\\((?<a>[0-9]+),(?<b>[0-9]+)\\)".toRegex()

private fun Day3Part1(lines: List<String>): Int {
    val input = lines.joinToString(separator = "")

    val matches = mul
        .findAll(input)
        .toList()

    return matches.sumOf { result ->
        result.groupValues[1].toInt() * result.groupValues[2].toInt()
    }
}

private fun Day3Part2(lines: List<String>): Int {
    val input = lines.joinToString(separator = "")
    var index = 0
    var enabled = true
    var sum = 0

    while (index < input.length) {
        // Find the next mul
        val match = mul.find(input, index) ?: break

        // If not enabled, skip along to the next do
        if (!enabled) {
            index = input.indexOf("do()", startIndex = index) + 4
            enabled = true
        } else {
            val dontIndex = input.indexOf("don't()", startIndex = index)

            if (dontIndex != -1 && dontIndex < match.range.first) {
                enabled = false
                index = dontIndex + 6
            } else {
                sum += match.groupValues[1].toInt() * match.groupValues[2].toInt()
                index = match.range.last + 1
            }
        }
    }

    return sum
}

fun main() {
    val input = input(day = 3)

    Day3Part1(input.sample()) shouldBe 161
    Day3Part1(input.main()) shouldBe 182780583

    Day3Part2(input.sample(part = 2)) shouldBe 48
    Day3Part2(input.main()) shouldBe 90772405
}