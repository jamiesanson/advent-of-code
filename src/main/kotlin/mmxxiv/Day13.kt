package mmxxiv

import io.kotest.matchers.shouldBe
import mmxxiv.input.input

private data class Machine(
    val a: Pair<Int, Int>,
    val b: Pair<Int, Int>,
    val prize: Pair<Long, Long>
)

private fun parseMachines(lines: List<String>): List<Machine> {
    return lines.windowed(size = 4, step = 4, partialWindows = true) {
        val (a, b, prize) = it

        val aX = a.substringAfter("X+").takeWhile(Char::isDigit).toInt()
        val aY = a.substringAfter("Y+").takeWhile(Char::isDigit).toInt()

        val bX = b.substringAfter("X+").takeWhile(Char::isDigit).toInt()
        val bY = b.substringAfter("Y+").takeWhile(Char::isDigit).toInt()

        val pX = prize.substringAfter("X=").takeWhile(Char::isDigit).toInt()
        val pY = prize.substringAfter("Y=").takeWhile(Char::isDigit).toInt()

        Machine(
            a = aX to aY,
            b = bX to bY,
            prize = pX.toLong() to pY.toLong()
        )
    }
}

/**
 * Simulate at most 100 presses of each button to find the cheapest combination of presses
 */
private fun Machine.cheapest(): Int? {
    for (aPress in 0 until 100) {
        for (bPress in 0 until 100) {
            val (pX, pY) = prize

            val (aX, aY) = a
            val (bX, bY) = b

            if (aX * aPress + bX * bPress == pX.toInt() && aY * aPress + bY * bPress == pY.toInt()) {
                return aPress * 3 + bPress
            }
        }
    }

    return null
}

/**
 * Use maths to find the cheapest combination of presses by solving a set of simultaneous equations
 */
private fun Machine.cheapestUsingMaths(): Long? {
    val (xA, yA) = a
    val (xB, yB) = b
    val (pA, pB) = prize

    val a = (pA * yB - pB * xB).toDouble() / (xA * yB - yA * xB)
    val b = (pB * xA - pA * yA).toDouble() / (xA * yB - yA * xB)

    if (a - a.toLong() == 0.0 && b - b.toLong() == 0.0) {
        return (a.toLong() * 3) + (b.toLong())
    }

    return null
}

private fun Day13Part1(lines: List<String>): Int {
    val machines = parseMachines(lines)

    return machines.mapNotNull { it.cheapest() }.sum()
}

private fun Day13Part2(lines: List<String>): Long {
    val machines = parseMachines(lines)
        .map { it.copy(prize = it.prize.first + 10000000000000L to it.prize.second + 10000000000000L) }

    return machines
        .mapNotNull { it.cheapestUsingMaths() }.sum()
}

fun main() {
    val input = input(day = 13)

    Day13Part1(input.sample()) shouldBe 480
    Day13Part1(input.main()) shouldBe 28262

    Day13Part2(input.sample()) shouldBe 875318608908L
    Day13Part2(input.main()) shouldBe 101406661266314L
}