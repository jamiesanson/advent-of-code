package mmxxi

import java.lang.Math.abs

fun Day7Part1(input: List<String>): Int {
    val startingPos = input.first().split(",").map { it.toInt() }

    val min = startingPos.minOrNull()!!
    val max = startingPos.maxOrNull()!!

    var minFuel = Int.MAX_VALUE

    for (a in min..max) {
        val fuel = startingPos.map { abs(it - a) }.sum()
        if (fuel < minFuel) minFuel = fuel
    }

    return minFuel
}


fun Day7Part2(input: List<String>): Int {
    val startingPos = input.first().split(",").map { it.toInt() }

    fun Int.faketorial() = if (this <= 1) this else (1..this).reduce { acc, i -> acc + i }

    val min = startingPos.minOrNull()!!
    val max = startingPos.maxOrNull()!!

    var minFuel = Int.MAX_VALUE

    for (a in min..max) {
        val fuel = startingPos.map { abs(it - a).faketorial() }.sum()
        if (fuel < minFuel) minFuel = fuel
    }

    return minFuel
}
