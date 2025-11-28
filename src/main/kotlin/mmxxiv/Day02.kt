package mmxxiv

import io.kotest.matchers.shouldBe
import mmxxiv.input.input

private fun parseReports(lines: List<String>): List<List<Int>> {
    return lines.map { line ->
        line.split(" ").map { it.toInt() }
    }
}

private fun List<Int>.isSafe(): Boolean {
    val allIncreasing = windowed(2).all { (first, second) ->
        (second - first) in 1..3
    }

    val allDecreasing = windowed(2).all { (first, second) ->
        (first - second) in 1..3
    }

    return allIncreasing || allDecreasing
}

private fun Day2Part1(lines: List<String>): Int =
    parseReports(lines)
        .filter { report -> report.isSafe() }
        .size

private fun Day2Part2(lines: List<String>): Int {
    val reports = parseReports(lines)

    val isTolerable = reports
        .filter { !it.isSafe() }
        .count tolerable@{ report ->
            // Every permutation of report without given element
            for (index in report.indices) {
                val modified = report.toMutableList()
                modified.removeAt(index)

                if (modified.isSafe()) return@tolerable true
            }

            false
        }

    return reports.count { it.isSafe() } + isTolerable
}

fun main() {
    val input = input(day = 2)

    Day2Part1(input.sample()) shouldBe 2
    Day2Part1(input.main()) shouldBe 379

    Day2Part2(input.sample()) shouldBe 4
    Day2Part2(input.main()) shouldBe 430
}