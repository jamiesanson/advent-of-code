package mmxxiv

import io.kotest.matchers.shouldBe
import mmxxiv.input.input
import util.Point
import util.minus
import util.neighbours
import util.plus
import util.toPoints

private fun Day4Part1(lines: List<String>): Int {
    var found = 0
    val grid = lines.toPoints()

    val starting = grid.entries.filter { (_, char) -> char == 'X' }

    for ((point, _) in starting) {
        // Find the next M's
        val neighbours = point
            .neighbours(diagonallyAdjacent = true)
            .filter { grid[it] == 'M' }

        neighbours.forEach { neighbour ->
            val direction = neighbour - point

            if (grid[neighbour + direction] == 'A' && grid[neighbour + direction + direction] == 'S') {
                found++
            }
        }
    }

    return found
}

private fun Day4Part2(lines: List<String>): Int {
    var found = 0
    val grid = lines.toPoints()

    val starting = grid.entries.filter { (_, char) -> char == 'A' }

    for ((point, _) in starting) {
        val d1 = buildString {
            append(grid[Point(point.x - 1, point.y - 1)])
            append(grid[point])
            append(grid[Point(point.x + 1, point.y + 1)])
        }

        val d2 = buildString {
            append(grid[Point(point.x + 1, point.y - 1)])
            append(grid[point])
            append(grid[Point(point.x - 1, point.y + 1)])
        }

        val downRight = d1 == "MAS" || d1 == "SAM"
        val downLeft = d2 == "MAS" || d2 == "SAM"

        if (downLeft && downRight) {
            found++
        }
    }

    return found
}

fun main() {
    val input = input(day = 4)

    Day4Part1(input.sample()) shouldBe 18
    Day4Part1(input.main()) shouldBe 2530

    Day4Part2(input.sample()) shouldBe 9
    Day4Part2(input.main()) shouldBe 1921
}