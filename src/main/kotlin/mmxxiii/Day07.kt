package mmxxiii

import io.kotest.matchers.shouldBe
import mmxxiii.input.input
import util.Input

data class Hand(
    val cards: List<Char>,
    val bid: Int,
    val jsAsJokers: Boolean,
) : Comparable<Hand> {
    private val strength: Int
        get() {
            val cardGroups = if (jsAsJokers && cards.contains('J')) {
                val groups = cards.groupBy { it }.toMutableMap()

                val jokers = requireNotNull(groups.remove('J')) { "No jokers" }

                if (groups.isEmpty()) {
                    mapOf('J' to jokers)
                } else {
                    val commonCard = groups.maxBy { it.value.size }
                    groups[commonCard.key] = commonCard.value + jokers

                    groups
                }
            } else {
                cards.groupBy { it }
            }

            val mostRepetitions = cardGroups.map { it.value.size }.max()

            return when (cardGroups.size) {
                5 -> 1 // High card
                4 -> 2 // One pair
                3 -> {
                    if (mostRepetitions == 2) {
                        3 // Two-pair
                    } else {
                        4 // Three of a kind
                    }
                }
                2 -> {
                    if (mostRepetitions == 3){
                        5 // Full house
                    } else {
                        6 // Four of a kind
                    }
                }
                // Five of a kind
                else -> 7
            }
        }

    private val Char.cardValue: Int
        get() = when (this@cardValue) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> if (jsAsJokers) 1 else 11
            'T' -> 10
            else -> digitToInt()
        }

    override fun compareTo(other: Hand): Int {
        val comparitiveStrength = strength - other.strength

        return if (comparitiveStrength == 0) {
            cards.zip(other.cards).forEach { (ths, other) ->
                val cardStrength = ths.cardValue - other.cardValue
                if (cardStrength != 0) return cardStrength
            }

            return 0
        } else {
            comparitiveStrength
        }
    }
}

fun List<String>.parseHands(jsAsJokers: Boolean = false): List<Hand> {
    return map { line ->
        Hand(
            cards = line.take(5).toList(),
            bid = line.takeLastWhile { it.isDigit() }.toInt(),
            jsAsJokers = jsAsJokers,
        )
    }
}

fun Day07Part1(input: List<String>): Int {
    val hands = input.parseHands()

    val winnings = hands.sorted()
        .mapIndexed { index, hand ->
            hand.bid * (index + 1)
        }

    return winnings.sum()
}


fun Day07Part2(input: List<String>): Int {
    val hands = input.parseHands(jsAsJokers = true)

    val winnings = hands.sorted()
        .mapIndexed { index, hand ->
            hand.bid * (index + 1)
        }

    return winnings.sum()
}

fun main() {
    val input = input(day = 7)

    Day07Part1(input.sample()) shouldBe 6440
    Day07Part1(input.main()) shouldBe 247961593

    Day07Part2(input.sample()) shouldBe 5905
    Day07Part2(input.main()) shouldBe 248750699
}