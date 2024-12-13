package mmxxiiii

import io.kotest.matchers.shouldBe
import mmxxiiii.input.input

private fun Day11(lines: List<String>, iterations: Int): Long {
    val stones = lines.first().split(" ").map { it.toLong() }.toMutableList()
    val cache = mutableMapOf<String, Long>()

    // Recursively process an element, using a cache to avoid recalculating the same values
    fun eventualStones(seed: Long, iterations: Int): Long =
        cache.getOrPut("$seed-$iterations") {
            val stringSeed = seed.toString()
            when {
                iterations == 0 -> 1
                seed == 0L -> eventualStones(seed = 1L, iterations = iterations - 1)
                stringSeed.length % 2 == 0 -> {
                    val half = stringSeed.length / 2
                    val left = stringSeed.substring(0, half).toLong()
                    val right = stringSeed.substring(half).toLong()
                    eventualStones(left, iterations - 1) + eventualStones(right, iterations - 1)
                }

                else -> eventualStones(seed * 2024, iterations - 1)
            }
        }

    return stones.sumOf { stone ->
        eventualStones(stone, iterations)
    }
}

fun main() {
    val input = input(day = 11)

    // Part 1
    Day11(input.sample(), iterations = 6) shouldBe 22
    Day11(input.sample(), iterations = 25) shouldBe 55312

    Day11(input.main(), iterations = 25) shouldBe 217812

    // Part 2
    Day11(input.main(), iterations = 75) shouldBe 259112729857522L
}