package mmxxv

import io.kotest.matchers.shouldBe
import mmxxv.input.input

private fun Day01Part1(input: List<String>): Long {
    var pos = 50
    var count = 0

    for (line in input) {
        val rot = line.drop(1).toInt()

        pos = when (line[0]) {
            'L' -> pos - rot
            'R' -> pos + rot
            else -> error("Invalid line: $line")
        }.mod(100)

        if (pos == 0) count++
    }

    return count.toLong()
}

/**
 * I'm sure there's a math solution, but I'm rusty, so this just does it iteratively :shrug:
 */
private fun Day01Part2(input: List<String>): Long {
    var pos = 50
    var count = 0

    for (line in input) {
        val rot = line.drop(1).toInt()
        val step = if (line[0] == 'L') -1 else 1

        repeat(rot) {
            var newPos = pos + step

            when {
                newPos > 99 -> newPos = 0
                newPos < 0 -> newPos = 99
            }

            if (newPos == 0) count++

            pos = newPos
        }
    }

    return count.toLong()
}

fun main() {
    val input = input(day = 1)

    Day01Part1(input.sample()) shouldBe 3
    Day01Part1(input.main()) shouldBe 1152

    Day01Part2(input.sample()) shouldBe 6
    Day01Part2(input.main()) shouldBe 6671
}