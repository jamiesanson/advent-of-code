package mmxxiii

import io.kotest.matchers.shouldBe
import mmxxiii.input.Input
import util.Direction
import util.Direction.*
import util.Point
import util.step

private data class Instruction(val direction: Direction, val count: Int, val color: String)

private fun parseInstructions(input: List<String>): List<Instruction> {
    val directions = mapOf('U' to Up, 'D' to Down, 'L' to Left, 'R' to Right)
    return input
        .map { it.split(" ") }
        .map { (dir, count, colour) ->
            Instruction(
                direction = directions[dir.first()]!!,
                count = count.toInt(),
                color = colour.removeSurrounding("(", ")"),
            )
        }
}

private fun countInternalPoints(vertices: List<Point>): Long {
    return vertices.windowed(2) { (first, second) ->
        (first.x.toLong() * second.y.toLong()) - (first.y.toLong() * second.x.toLong())
    }.sum() / 2
}

private fun areaWithinInstructions(instructions: List<Instruction>): Long {
    val vertices = mutableListOf(Point(0, 0))
    var boundaryPoints = 0

    instructions.forEach { (direction, count, _) ->
        boundaryPoints += count
        vertices += vertices.last().step(direction, count)
    }

    // Shoelace formula
    val internalPoints = countInternalPoints(vertices)

    // Picks theorem (offset by two)
    return internalPoints + (boundaryPoints / 2) + 1
}

fun Day18Part1(input: List<String>): Long {
    val instructions = parseInstructions(input)
    return areaWithinInstructions(instructions)
}

fun Day18Part2(input: List<String>): Long {
    val directions = listOf(Right, Down, Left, Up)
    val instructions = parseInstructions(input)
        .map { (_, _, color) ->
            val (number, dirIndex) = color.removePrefix("#").let {
                it.take(5).toInt(radix = 16) to it.takeLast(1).toInt()
            }

            Instruction(direction = directions[dirIndex], count = number, color = color)
        }

    return areaWithinInstructions(instructions)
}

fun main() {
    val input = Input("18")

    Day18Part1(input.sample()) shouldBe 62
    Day18Part1(input.main()) shouldBe 46359

    Day18Part2(input.sample()) shouldBe 952408144115L
    Day18Part2(input.main()) shouldBe 59574883048274L
}