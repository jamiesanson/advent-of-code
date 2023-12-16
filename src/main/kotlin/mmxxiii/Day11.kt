package mmxxiii

import io.kotest.matchers.shouldBe
import mmxxiii.input.Input
import kotlin.math.abs

fun List<String>.rotate(): List<String> {
    val chars = map { it.toList() }
    val new = MutableList(chars.first().size) {
        MutableList(chars.size) { '.' }
    }

    for (y in chars.indices) {
        for (x in chars.first().indices) {
            new[new.size - x - 1][y] = chars[y][x]
        }
    }

    return new.map { it.joinToString("") }
}

private typealias Galaxy = Pair<Long, Long>

private fun List<List<Char>>.findGalaxies(): List<Galaxy> {
    val galaxies = mutableListOf<Pair<Long, Long>>()
    forEachIndexed { y, row ->
        row.forEachIndexed { x, c ->
            if (c == '#') {
                galaxies += x.toLong() to y.toLong()
            }
        }
    }
    return galaxies
}

private fun List<Galaxy>.toPairings(): List<Pair<Galaxy, Galaxy>> {
    return flatMapIndexed { index: Int, galaxy: Galaxy ->
        drop(index + 1).map {
            galaxy to it
        }
    }
}

private fun List<Pair<Galaxy, Galaxy>>.distances(): List<Long> {
    return map {
        abs(it.first.first - it.second.first) + abs(it.first.second - it.second.second)
    }
}

fun Day11(input: List<String>, expansionMultiplier: Int = 1_000_000): Long {
    val yEmpty = input.mapIndexedNotNull { index, s -> index.takeIf { s.all { it == '.' } } }
    val xEmpty = input.rotate()
        .mapIndexedNotNull { index, s -> index.takeIf { s.all { it == '.' } } }
        .map { input.first().length - it - 1 }

    val multiplier = expansionMultiplier - 1

    val pairings = input
        .map { it.toList() }
        .findGalaxies()
        .map { (x, y) ->
            (x + (xEmpty.count { it < x } * multiplier)) to (y + (yEmpty.count { it < y } * multiplier))
        }
        .toPairings()

    return pairings
        .distances()
        .sum()
}

fun main() {
    val input = Input("11")

    Day11(input.sample(), expansionMultiplier = 2) shouldBe 374
    Day11(input.sample(), expansionMultiplier = 10) shouldBe 1030
    Day11(input.sample(), expansionMultiplier = 100) shouldBe 8410

    Day11(input.main(), expansionMultiplier = 2) shouldBe 9608724
    Day11(input.main(), expansionMultiplier = 1_000_000) shouldBe 904633799472L
}