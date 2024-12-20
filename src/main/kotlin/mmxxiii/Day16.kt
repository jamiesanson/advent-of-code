package mmxxiii

import io.kotest.matchers.shouldBe
import mmxxiii.input.input
import util.Direction.*
import util.Input
import util.Direction
import util.Point
import util.step
import util.toPoints

private typealias Contraption = Map<Point, Char>

private fun Direction.reflect(char: Char): List<Direction> {
    return when {
        char == '|' && horizontal ->
            listOf(Up, Down)

        char == '-' && vertical ->
            listOf(Left, Right)

        char == '\\' -> {
            listOf(
                when (this) {
                    Up -> Left
                    Down -> Right
                    Left -> Up
                    Right -> Down
                }
            )
        }

        char == '/' -> {
            listOf(
                when (this) {
                    Up -> Right
                    Down -> Left
                    Left -> Down
                    Right -> Up
                }
            )
        }

        else -> listOf(this)
    }
}


/**
 * Traverse the contraction and return a list of visited points when reaching somewhere terminal,
 * or when we end up in a cycle
 */
private fun Contraption.traverseInto(
    point: Point,
    direction: Direction,
    collection: MutableMap<Point, List<Direction>>,
    debug: Boolean = false,
) {
    val visitedDirections = collection[point] ?: emptyList()

    if (!containsKey(point) || direction in visitedDirections) {
        return
    } else {
        val nextDirs = direction.reflect(getValue(point))

        if (debug) {
            println("$point | $direction | ${getValue(point)} | $nextDirs")
        }

        collection[point] = visitedDirections + direction

        nextDirs.forEach {
            traverseInto(
                point = point.step(it),
                direction = it,
                collection = collection,
                debug = debug,
            )
        }
    }
}

fun Day16Part1(input: List<String>): Int {
    val contraption = input.toPoints()

    val visited = mutableMapOf<Point, List<Direction>>()

    contraption
        .traverseInto(
            point = Point(0, 0),
            direction = Right,
            collection = visited
        )

    return visited.size
}

fun Day16Part2(input: List<String>): Int {
    val contraption = input.toPoints()

    val energiseCounts = mutableListOf<Int>()

    val startingConditions = listOf(
        input.first().indices.flatMap {
            listOf(
                Point(it, 0) to Down,
                Point(it, input.lastIndex) to Up
            )
        },
        input.indices.flatMap {
            listOf(
                Point(0, it) to Right,
                Point(input.first().lastIndex, it) to Left,
            )
        },
    ).flatten()

    for ((point, direction) in startingConditions) {
        val visited = mutableMapOf<Point, List<Direction>>()

        contraption.traverseInto(point, direction, visited)

        energiseCounts += visited.size
    }

    return energiseCounts.max()
}


fun main() {
    val input = input(day = 16)

    Day16Part1(input.sample()) shouldBe 46
    Day16Part1(input.main()) shouldBe 7798

    Day16Part2(input.sample()) shouldBe 51
    Day16Part2(input.main()) shouldBe 8026
}