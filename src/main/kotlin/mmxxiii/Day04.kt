package mmxxiii

import io.kotest.matchers.shouldBe
import mmxxiii.input.input
import util.Input
import kotlin.math.pow

private data class Card(
    val id: Int,
    val winningNumbers: List<Int>,
    val numbers: List<Int>,
)

private fun List<String>.parseCards(): List<Card> = map { line ->
    Card(
        id = line
            .substringBefore(':')
            .drop(5)
            .trim()
            .toInt(),
        winningNumbers = line
            .substringAfter(':')
            .takeWhile { it != '|' }
            .split(' ')
            .mapNotNull { it.trim().toIntOrNull() },
        numbers = line
            .substringAfter('|')
            .split(' ')
            .mapNotNull { it.trim().toIntOrNull() }
    )
}

private fun Card.findMatchingNumbers(): Set<Int> =
    numbers.intersect(winningNumbers.toSet())

private fun Card.calculatePoints(): Int {
    val matchingNumbers = findMatchingNumbers()

    return if (matchingNumbers.isNotEmpty()) {
        2.0.pow(matchingNumbers.size - 1).toInt()
    } else {
        0
    }
}


private fun MutableMap<Int, Int>.update(key: Int, block: (Int) -> Int) {
    val value = getValue(key)
    set(key, block(value))
}

fun Day04Part1(input: List<String>): Int {
    val cards = input.parseCards()

    return cards.sumOf { it.calculatePoints() }
}

fun Day04Part2(input: List<String>): Int {
    val cards = input.parseCards()

    val cardCounts = cards
        .associate { it.id to 1 }
        .toMutableMap()

    cards.forEach { card ->
        val matchCount = card.findMatchingNumbers().size
        val additionMultiplier = cardCounts[card.id] ?: 1

        repeat(matchCount) { offset ->
            cardCounts.update(card.id + offset + 1) {
                it + additionMultiplier
            }
        }
    }

    return cardCounts
        .toList()
        .sumOf { it.second }
}


fun main() {
    val input = input(day = 4)

    Day04Part1(input.sample()) shouldBe 13
    Day04Part1(input.main()) shouldBe 26443

    Day04Part2(input.sample()) shouldBe 30
    Day04Part2(input.main()) shouldBe 6284877
}