import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.abs

fun Day5Part1(input: List<String>): Int {

    data class Point(val x: Int, val y: Int)

    val lines = input.map {
        val (to, from) = it.split(" -> ")
        Point(x = to.split(",").first().toInt(), y = to.split(",").last().toInt()) to
                Point(x = from.split(",").first().toInt(), y = from.split(",").last().toInt())
    }

    val hits = mutableMapOf<Point, Int>()

    lines
        // Get rid of the non-vertical or non-horizontal ones
        .filter { (from, to) -> from.x == to.x || from.y == to.y }
        .forEach {
            val (from, to) = it
            for (x in min(from.x, to.x)..max(from.x, to.x)) {
                for (y in min(from.y, to.y)..max(from.y, to.y)) {
                    hits[Point(x, y)] = (hits[Point(x, y)] ?: 0) + 1
                }
            }
        }

    return hits.filterValues { it >= 2 }.size
}


fun Day5Part2(input: List<String>): Int {

    data class Point(val x: Int, val y: Int)

    val lines = input.map {
        val (to, from) = it.split(" -> ")
        Point(x = to.split(",").first().toInt(), y = to.split(",").last().toInt()) to
                Point(x = from.split(",").first().toInt(), y = from.split(",").last().toInt())
    }

    val hits = mutableMapOf<Point, Int>()

    lines
        // Keep vertical, horizonal
        .filter { (from, to) -> from.x == to.x || from.y == to.y }
        .forEach {
            val (from, to) = it
            for (x in min(from.x, to.x)..max(from.x, to.x)) {
                for (y in min(from.y, to.y)..max(from.y, to.y)) {
                    hits[Point(x, y)] = (hits[Point(x, y)] ?: 0) + 1
                }
            }
        }

    lines
        // Keep 45 degree diagonal
        .filter { (from, to) -> (abs(from.x - to.x) == abs(from.y - to.y)) }
        .onEach { println("Diagonal: $it") }
        .forEach {
            val (from, to) = it

            val first = if (from.x < to.x) from else to
            val second = if (first == to) from else to
            val rising = first.y < second.y

            for ((step, x) in (first.x..second.x).withIndex()) {
                val y = first.y + (step * if (rising) 1 else -1)
                hits[Point(x, y)] = (hits[Point(x, y)] ?: 0) + 1
            }
        }

    for (y in 0..9) {
        for (x in 0..9) {
            print(hits[Point(x, y)] ?: '.')
        }
        println()
    }

    return hits.filterValues { it >= 2 }.size
}
