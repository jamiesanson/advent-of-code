package mmxxv

import io.kotest.matchers.shouldBe
import mmxxv.input.input
import util.neighbours
import util.toPoints

private fun Day04Part1(input: List<String>): Long {
    val points = input.toPoints()

    return points.toList()
        .count { (point, char) ->
            if (char == '@') {
                point.neighbours(diagonallyAdjacent = true)
                    .count { points[it] == '@' } < 4
            } else {
                false
            }
        }.toLong()
}

private fun Day04Part2(input: List<String>): Long {
    val points = input.toPoints()
        .toMutableMap()

    while (true) {
        val toRemove = points.toList()
            .filter { (point, char) ->
                if (char == '@') {
                    point.neighbours(diagonallyAdjacent = true)
                        .count { points[it] == '@' } < 4
                } else {
                    false
                }
            }

        toRemove.forEach { (point, _) ->
            points[point] = 'x'
        }

        if (toRemove.isEmpty()) break
    }

    return points.count { it.value == 'x' }.toLong()
}

fun main() {
    val input = input(day = 4)

    Day04Part1(input.sample()) shouldBe 13
    Day04Part1(input.main()) shouldBe 1564

    Day04Part2(input.sample()) shouldBe 43
    Day04Part2(input.main()) shouldBe 9401
}