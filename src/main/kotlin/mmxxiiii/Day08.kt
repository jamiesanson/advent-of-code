package mmxxiiii

import io.kotest.matchers.shouldBe
import mmxxiiii.input.input
import util.Point
import util.minus
import util.plus
import util.toPoints

private fun MutableSet<Point>.addIfInBounds(xBound: IntRange, yBound: IntRange, point: Point): Boolean {
    if (point.x in xBound && point.y in yBound) {
        add(point)
        return true
    }

    return false
}

private fun List<String>.solve(withPropagation: Boolean): Int {
    val grid = toPoints()
    val candidates = grid.entries.filter { it.value != '.' }

    val xBound = 0 .. grid.keys.maxOf { it.x }
    val yBound = 0 .. grid.keys.maxOf { it.y }

    val antinodes = mutableSetOf<Point>()

    fun record(point: Point): Boolean =
        antinodes.addIfInBounds(xBound, yBound, point)

    for ((a, char) in candidates) {
        for ((b, _) in candidates.filter { it.value == char }) {
            if (a == b) continue

            // Add the points themselves
            if (withPropagation) {
                record(a)
                record(b)
            }

            val delta = b - a

            var candidate1 = a - delta
            while (record(candidate1)) {
                if (!withPropagation) break
                candidate1 -= delta
            }

            var candidate2 = b + delta
            while (record(candidate2)) {
                if (!withPropagation) break
                candidate2 += delta
            }
        }
    }

    return antinodes.size
}

private fun Day8Part1(lines: List<String>): Int {
    return lines.solve(withPropagation = false)
}

private fun Day8Part2(lines: List<String>): Int {
    return lines.solve(withPropagation = true)
}

fun main() {
    val input = input(day = 8)

    Day8Part1(input.sample()) shouldBe 14
    Day8Part1(input.main()) shouldBe 271

    Day8Part2(input.sample(part = 2)) shouldBe 9
    Day8Part2(input.sample()) shouldBe 34
    Day8Part2(input.main()) shouldBe 994
}