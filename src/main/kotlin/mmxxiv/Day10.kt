package mmxxiv

import io.kotest.matchers.shouldBe
import mmxxiv.input.input
import util.Point
import util.neighbours
import util.toPoints

private fun Map<Point, Char>.visitNext(point: Point, visitedPeaks: MutableCollection<Point>) {
    val neighbours = point.neighbours(diagonallyAdjacent = false)
    val value = this[point]!!.digitToInt()

    for (next in neighbours) {
        when (val nextValue = this[next]?.digitToIntOrNull()) {
            value + 1 -> {
                if (nextValue == 9) {
                    visitedPeaks.add(next)
                } else {
                    visitNext(next, visitedPeaks)
                }
            }
            // Out of bounds, not an int, or out of range
            else -> continue
        }
    }
}

private fun Map<Point, Char>.walkTrails(createMemo: () -> MutableCollection<Point>): Int {
    val trailheads = entries.filter { it.value == '0' }

    return trailheads.sumOf { (point, _) ->
        val visitedPeaks = createMemo()
        visitNext(point, visitedPeaks)

        visitedPeaks.size
    }
}

private fun Day10Part1(lines: List<String>): Int =
    lines.toPoints().walkTrails { mutableSetOf() }

private fun Day10Part2(lines: List<String>): Int =
    lines.toPoints().walkTrails { mutableListOf() }

fun main() {
    val input = input(day = 10)

    Day10Part1(input.sample(part = 1)) shouldBe 36
    Day10Part1(input.main()) shouldBe 841

    Day10Part2(input.sample(part = 2)) shouldBe 81
    Day10Part2(input.main()) shouldBe 1875
}