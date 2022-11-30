package mmxxi

import helpers.Point
import helpers.neighbours

fun Day11Part1(input: List<String>): Int {
    val dumbos = input
        .map { row ->
            row
                .trim()
                .toCharArray()
                .map { it.digitToInt() }
                .toMutableList()
        }
        .toMutableList()
        .flatMapIndexed { y: Int, ints: MutableList<Int> ->
            ints.mapIndexed { x, i ->
                (x to y) to i
            }
        }
        .toMap()
        .toMutableMap() // Map<Point, Int> - position to power level.

    fun Point.isValid(): Boolean {
        val (x, y) = this
        return x in 0..9 && y in 0..9
    }

    var flashCount = 0

    repeat(100) {
        // Increase energy
        dumbos.forEach { (key, value) -> dumbos[key] = value + 1 }

        // Flashbang out
        do {
            val flashing = dumbos.filterValues { it > 9 }

            // Reset em
            flashing.forEach { (key, _) -> dumbos[key] = 0 }

            flashing
                .flatMap { (point, _) -> point.neighbours(diagonallyAdjacent = true) }
                .filter { it.isValid() && dumbos[it] != 0 /* Don't increment the freshly reset ones */ }
                .forEach { dumbos[it] = dumbos[it]!! + 1 }
        } while (flashing.isNotEmpty())

        flashCount += dumbos.count { it.value == 0 }
    }

    return flashCount
}


fun Day11Part2(input: List<String>): Int {
    val dumbos = input
        .map { row ->
            row
                .trim()
                .toCharArray()
                .map { it.digitToInt() }
                .toMutableList()
        }
        .toMutableList()
        .flatMapIndexed { y: Int, ints: MutableList<Int> ->
            ints.mapIndexed { x, i ->
                (x to y) to i
            }
        }
        .toMap()
        .toMutableMap() // Map<Point, Int> - position to power level.

    fun Point.isValid(): Boolean {
        val (x, y) = this
        return x in 0..9 && y in 0..9
    }

    var i = 0
    while (true) {
        // Increase energy
        dumbos.forEach { (key, value) -> dumbos[key] = value + 1 }

        // Flashbang out
        do {
            val flashing = dumbos.filterValues { it > 9 }

            // Reset em
            flashing.forEach { (key, _) -> dumbos[key] = 0 }

            flashing
                .flatMap { (point, _) -> point.neighbours(diagonallyAdjacent = true) }
                .filter { it.isValid() && dumbos[it] != 0 /* Don't increment the freshly reset ones */ }
                .forEach { dumbos[it] = dumbos[it]!! + 1 }
        } while (flashing.isNotEmpty())

        i++

        if (dumbos.count { it.value == 0 } == 100) return i
    }

    @Suppress("UNREACHABLE_CODE")
    return 1
}