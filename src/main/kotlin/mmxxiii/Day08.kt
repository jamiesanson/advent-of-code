package mmxxiii

import io.kotest.matchers.shouldBe
import mmxxiii.input.input
import util.Input

data class LRMap(
    val instructions: List<Char>,
    val nodes: Map<String, Pair<String, String>>
) {
    fun String.left(): String = nodes[this]!!.first
    fun String.right(): String = nodes[this]!!.second

    data class TraversalResult(val steps: Int)

    fun traverse(from: String, to: String): TraversalResult {
        return countTraversal(from) { it == to }
    }

    fun countTraversal(from: String, to: (String) -> Boolean): TraversalResult {
        var instructionIndex = 0
        var stepCount = 0
        var current = from

        while (!to(current)) {
            current = when (val instruction = instructions[instructionIndex]) {
                'L' -> current.left()
                'R' -> current.right()
                else -> error("Undefined instruction $instruction")
            }
            stepCount++
            instructionIndex = if (instructionIndex == instructions.lastIndex) {
                0
            } else {
                instructionIndex + 1
            }
        }

        return TraversalResult(stepCount)
    }
}

private fun List<String>.parseMap(): LRMap {
    return LRMap(
        instructions = first().toList().filter { it == 'L' || it == 'R' },
        nodes = drop(2)
            .associate { line ->
                val key = line.takeWhile { it != ' ' }
                val value = line
                    .dropLast(1)
                    .takeLastWhile { it != '(' }
                    .split(", ")

                key to (value[0] to value[1])
            }
    )
}

private fun greatestCommonDenominator(a: Long, b: Long): Long {
    var c = a
    var d = b
    while (d > 0) {
        val temp = d
        d = c % d
        c = temp
    }
    return c
}

private fun leastCommonMultiple(a: Long, b: Long): Long =
    a * (b / greatestCommonDenominator(a, b))

private fun List<Int>.leastCommonMultiple(): Long {
    var result = get(0).toLong()
    for (i in 1 until size) {
        result = leastCommonMultiple(result, get(i).toLong())
    }
    return result
}

fun Day08Part1(input: List<String>): Int {
    val map = input.parseMap()

    val result = map.traverse(from = "AAA", to = "ZZZ")

    return result.steps
}

fun Day08Part2(input: List<String>): Long {
    val map = input.parseMap()

    val startingPoints = map.nodes.keys.filter { it.endsWith("A") }

    val minSteps = startingPoints.map { start ->
        map.countTraversal(from = start) { it.endsWith("Z")}.steps
    }

    return minSteps.leastCommonMultiple()
}

fun main() {
    val input = input(day = 8)

    Day08Part1(input.sample()) shouldBe 2
    Day08Part1(input.main()) shouldBe 19241

    Day08Part2(input.sample(part = 2)) shouldBe 6
    Day08Part2(input.main()) shouldBe 9606140307013
}