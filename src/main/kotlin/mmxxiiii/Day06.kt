package mmxxiiii

import io.kotest.matchers.shouldBe
import mmxxiiii.input.input
import util.Direction
import util.Point
import util.step
import util.toPoints

private sealed interface Path {
    data class Found(val path: Set<Point>): Path
    data object Loops: Path
}

private fun walk(startPoint: Point, grid: Map<Point, Char>): Path {
    var position = startPoint
    var direction = Direction.Up

    val seen = mutableSetOf<Pair<Point, Direction>>()

    while (seen.add(position to direction)) {
        val next = position.step(direction)

        when (grid[next]) {
            '#' -> direction = direction.rotate()
            '.', '^' -> position = next
            else -> return Path.Found(seen.unzip().first.toSet())
        }
    }

    return Path.Loops
}

private fun Day6Part1(lines: List<String>): Int {
    val grid = lines.toPoints()
    val startPoint = grid.entries.find { (_, char) -> char == '^' }!!.key

    return (walk(startPoint, grid) as Path.Found).path.size
}

private fun Day6Part2(lines: List<String>): Int {
    val grid = lines.toPoints()
    val startPoint = grid.entries.find { (_, char) -> char == '^' }!!.key
    val defaultPath = walk(startPoint, grid) as Path.Found
    val route = defaultPath.path.drop(1)

    return route.count { point ->
        val testGrid = buildMap {
            putAll(grid)
            put(point, '#')
        }

        walk(startPoint, testGrid) is Path.Loops
    }
}

fun main() {
    val input = input(day = 6)

    Day6Part1(input.sample()) shouldBe 41
    Day6Part1(input.main()) shouldBe 4559

    Day6Part2(input.sample()) shouldBe 6
    Day6Part2(input.main()) shouldBe 1604
}

