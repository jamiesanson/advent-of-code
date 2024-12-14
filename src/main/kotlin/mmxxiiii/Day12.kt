package mmxxiiii

import io.kotest.matchers.shouldBe
import mmxxiiii.input.input
import util.Direction
import util.Point
import util.minus
import util.neighbours
import util.step
import util.toPoints

private typealias Region = Set<Point>

private fun Point.scanRegion(grid: Map<Point, Char>): Region {
    val points = mutableSetOf<Point>()

    points.add(this)

    fun traverse(current: Point, acc: MutableSet<Point>) {
        val validNeighbours = current.neighbours(diagonallyAdjacent = false)
            .filter { grid[it] == grid[current] }

        for (neighbour in validNeighbours) {
            if (neighbour !in acc) {
                acc.add(neighbour)
                traverse(neighbour, acc)
            }
        }
    }

    traverse(this, points)

    return points
}

private fun Map<Point, Char>.toRegions(): List<Region> {
    val regions = mutableListOf<Region>()

    entries.forEach { (point, _) ->
        if (regions.none { point in it }) {
            regions.add(point.scanRegion(this))
        }
    }

    return regions
}

private fun Region.border(): List<Point> {
    val border = mutableListOf<Point>()
    for (point in this) {
        val neighbours = point.neighbours(diagonallyAdjacent = false)

        for (neighbour in neighbours) {
            if (neighbour !in this) {
                border.add(neighbour)
            }
        }
    }

    return border
}

private fun Region.borderLength(): Int {
    return border().size
}

private fun Region.sides(grid: Map<Point, Char>): Int {
    var corners = 0

    fun isCorner(point: Point, directions: Pair<Direction, Direction>): Boolean {
        val (dir1, dir2) = directions
        if (grid[point.step(dir1)] != grid[point] && grid[point.step(dir2)] != grid[point]) {
            return true
        }

        if (grid[point.step(dir1)] == grid[point] && grid[point.step(dir2)] == grid[point]) {
            return grid[point.step(dir1).step(dir2)] != grid[point]
        }

        return false
    }

    for (point in this) {
        if (isCorner(point, Direction.Up to Direction.Left)) corners++
        if (isCorner(point, Direction.Up to Direction.Right)) corners++
        if (isCorner(point, Direction.Down to Direction.Left)) corners++
        if (isCorner(point, Direction.Down to Direction.Right)) corners++
    }

    return corners
}

private fun Day12Part1(lines: List<String>): Int {
    val grid = lines.toPoints()
    val regions = grid.toRegions()

    return regions.sumOf { region ->
        region.size * region.borderLength()
    }
}

private fun Day12Part2(lines: List<String>): Int {
    val grid = lines.toPoints()
    val regions = grid.toRegions()

    return regions.sumOf { region ->
        region.size * region.sides(grid)
    }
}

fun main() {
    val input = input(day = 12)

    Day12Part1(input.sample()) shouldBe 1930
    Day12Part1(input.main()) shouldBe 1549354

    Day12Part2(input.sample()) shouldBe 1206
    Day12Part2(input.main()) shouldBe 937032
}