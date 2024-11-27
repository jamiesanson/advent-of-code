package mmxxiii

import io.kotest.matchers.shouldBe
import mmxxiii.input.input
import util.Input
import util.Point
import util.neighbours
import util.toPoints
import kotlin.math.pow
import kotlin.math.sqrt

private typealias City = Map<Point, Int>

private fun Point.distanceTo(point: Point): Int {
    return sqrt((x - point.x).toDouble().pow(2.0) + (y - point.y).toDouble().pow(2.0)).toInt()
}

private fun City.traverse(
    start: Point,
    target: Point,
    visited: List<Point> = listOf(start),
): List<Point> {
    fun fitsCriteria(point: Point): Boolean {
        if (!this@traverse.containsKey(point)) return false

        if (point in visited) return false

        val last = visited.takeLast(3)

        val overrunsLine = last.size == 3 && (last + point).run {
            distinctBy { it.x }.size == 1 || distinctBy { it.y }.size == 1
        }

        return !overrunsLine
    }

    if (start != target) {
        val candidates = visited
            .last()
            .neighbours(diagonallyAdjacent = false)
            .also { println(it) }
            .filter(::fitsCriteria)

        for (candidate in candidates) {
            traverse(candidate, target, visited + start)
        }
    }

    return visited
}


private fun City.print(visited: List<Point>) {
    val ansiReset = "\u001B[0m"
    val ansiGreen = "\u001B[32;1m"

    toList().groupBy { it.first.y }.values
        .forEach { row ->
            row.forEach { (point, value) ->
                if (point in visited) {
                    print(ansiGreen + "$value" + ansiReset)
                } else {
                    print("$value")
                }
            }

            println()
        }
}

fun Day17Part1(input: List<String>): Int {
    val city = input.toPoints().mapValues { it.value.digitToInt() }
    val start = Point(0,0)

    val visited = city
        .traverse(start = start, target = city.keys.last())

    city.print(visited)

    return visited.sumOf { city.getValue(it) }
}

fun main() {
    val input = input(day = 17)

    Day17Part1(input.sample()) shouldBe 102
}