package mmxxiii

import io.kotest.matchers.shouldBe
import mmxxiii.input.input
import util.Input

private const val RoundRock = 'O'
private const val SquareRock = '#'

private fun Iterable<String>.rotate(): List<String> = buildList {
    val strings = this@rotate.toMutableList()
    var i = 0
    while (strings.isNotEmpty()) {
        add(buildString(strings.size) { for (string in strings) append(string[i]) })
        i++
        strings.removeAll { it.length == i }
    }
}

private fun List<String>.spin(): List<String> = List(4) {}.fold(this) { acc, _ ->
    acc.rotate().map { it.rollToEnd() }
}

private fun String.rollToEnd(): String {
    return split(SquareRock)
        .map { it.toList().sortedDescending() }
        .joinToString(SquareRock.toString()) { it.joinToString("") }
        .reversed()
}

private fun String.weight(): Int {
    return withIndex()
        .sumOf { (index, char) -> if (char == RoundRock) index + 1 else 0 }
}

fun Day14(input: List<String>, cycles: Int = 0): Int {
    if (cycles == 0) {
        return input.rotate().map { it.rollToEnd() }.sumOf { it.weight() }
    }

    var platform = input

    val cache = mutableMapOf(platform to 0)

    for (i in 1..cycles) {
        platform = platform.spin()//.debug()

        val j = cache.getOrPut(platform) { i }
        // Repeat until we find a cycle
        if (i != j) {
            // Our final state is "cached index" + ("elapsed cycles" modulo "cycle length")
            platform = cache.keys.elementAt(j + (cycles - i) % (i - j))
            break
        }
    }

    return platform.rotate().sumOf { it.reversed().weight() }
}

fun main() {
    val input = input(day = 14)

    Day14(input.sample()) shouldBe 136
    Day14(input.main()) shouldBe 109833

    Day14(input.sample(), cycles = 1_000_000_000) shouldBe 64
    Day14(input.main(), cycles = 1_000_000_000) shouldBe 99875
}