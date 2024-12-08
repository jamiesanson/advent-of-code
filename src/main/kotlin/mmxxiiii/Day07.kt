package mmxxiiii

import io.kotest.matchers.shouldBe
import mmxxiiii.input.input

private fun List<String>.toCalibrations(): List<Pair<Long, List<Long>>> =
    map { line ->
        val value = line.takeWhile { it != ':' }.toLong()
        val equation = line.dropWhile { it != ':' }.drop(2).split(" ").map { it.toLong() }

        value to equation
    }

private enum class Operator {
    Plus, Multiply, Concat;

    operator fun invoke(a: Long, b: Long): Long = when (this) {
        Plus -> a + b
        Multiply -> a * b
        Concat -> "$a$b".toLong()
    }
}

private fun canBeCalibrated(value: Long, equation: List<Long>, operators: Set<Operator>): Boolean {
    fun solveAtIndex(acc: Long, index: Int): Boolean {
        // If we overshoot, return early
        if (acc > value) return false

        // If we're at the end of the equation, check if we've reached the target value
        if (index + 1 >= equation.size) return acc == value

        // Recursively try every operator
        return operators.any { operator ->
            solveAtIndex(operator(acc, equation[index + 1]), index + 1)
        }
    }

    return solveAtIndex(equation.first(), 0)
}

private fun Day7Part1(lines: List<String>): Long {
    val calibrations = lines.toCalibrations()

    return calibrations
        .filter { (value, equation) ->
            canBeCalibrated(value, equation, operators = setOf(Operator.Plus, Operator.Multiply))
        }
        .sumOf { it.first }
}

private fun Day7Part2(lines: List<String>): Long {
    val calibrations = lines.toCalibrations()

    return calibrations
        .filter { (value, equation) ->
            canBeCalibrated(value, equation, operators = setOf(Operator.Plus, Operator.Multiply, Operator.Concat))
        }
        .sumOf { it.first }
}

fun main() {
    val input = input(day = 7)

    Day7Part1(input.sample()) shouldBe 3749
    Day7Part1(input.main()) shouldBe 267566105056L

    Day7Part2(input.sample()) shouldBe 11387
    Day7Part2(input.main()) shouldBe 116094961956019L
}