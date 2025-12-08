package mmxxv

import io.kotest.matchers.shouldBe
import mmxxv.input.input
import util.Point
import util.debug
import util.toPoints
import kotlin.collections.dropLast
import kotlin.collections.set
import kotlin.text.lastIndex

private val Point.below: Point
    get() =
        Point(x, y + 1)

private val Point.left: Point
    get() =
        Point(x - 1, y)

private val Point.right: Point
    get() =
        Point(x + 1, y)

private fun List<String>.rows(): List<List<Pair<Point, Char>>> {
    return mapIndexed { y, line ->
        line.mapIndexed { x, c -> Point(x, y) to c }
    }
}

private fun Day07Part1(input: List<String>): Int {
    val points = input.toPoints().toMutableMap()
    val start = points.toList().first { it.second == 'S' }.first
    points[start.below] = '|'

    var splits = 0

    for (row in input.rows()) {
        for ((point, char) in row) {
            if (char == '|') {
                if (points[point.below] == '^') {
                    splits++
                    points[point.below.left] = '|'
                    points[point.below.right] = '|'
                } else {
                    points[point.below] = '|'
                }
            }
        }
    }

    return splits
}

private fun Day07Part2(input: List<String>): Long {
    // The following calculates path visits via a Pascals Triangle-style algorithm.
    // Accumulate visits to a given column. 
    // For each row in the input, if we encounter a split, "ripple" the visit count outwards.
    val pathVisits = MutableList(input.size) { 0L }

    // Count one path visit at 'S'
    pathVisits[input.first().indexOf('S')] = 1

    for (row in input.rows()) {
        for ((point, char) in row) {
            if (char == '^') {
                // "Ripple" the visit count outwards, clear current position
                pathVisits[point.x - 1] += pathVisits[point.x]
                pathVisits[point.x + 1] += pathVisits[point.x]
                pathVisits[point.x] = 0
            }
        }
    }

    return pathVisits.sum()
}

fun main() {
    val input = input(day = 7)

    Day07Part1(input.sample()) shouldBe 21
    Day07Part1(input.main()) shouldBe 1672

    Day07Part2(input.sample()) shouldBe 40
    Day07Part2(input.main()) shouldBe 231229866702355L
}