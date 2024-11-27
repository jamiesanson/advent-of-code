package mmxxiii

import io.kotest.matchers.shouldBe
import mmxxiii.input.input
import util.Input

fun List<String>.findAllComponents(): List<Component> = buildList {
    this@findAllComponents.forEachIndexed { y, line ->
        var currentNumber = ""
        var startIndex = -1

        line.forEachIndexed { x, char ->
            when {
                char.isDigit() -> {
                    currentNumber += char
                    if (startIndex < 0) startIndex = x
                }

                else -> {
                    if (char != '.') add(Symbol(char, column = x, row = y))
                    if (currentNumber.isNotEmpty()) {
                        add(
                            PartsNumber(
                                number = currentNumber.toInt(),
                                row = y,
                                startIndex = startIndex,
                                endIndex = x - 1
                            )
                        )

                        currentNumber = ""
                        startIndex = -1
                    }
                }
            }
        }

        if (currentNumber.isNotEmpty()) {
            add(
                PartsNumber(
                    number = currentNumber.toInt(),
                    row = y,
                    startIndex = startIndex,
                    endIndex = line.length - 1
                )
            )
        }
    }
}

sealed interface Component

data class Symbol(
    val value: Char,
    val column: Int,
    val row: Int,
) : Component

data class PartsNumber(
    val number: Int,
    val row: Int,
    val startIndex: Int,
    val endIndex: Int,
) : Component {
    val surroundingXRange = IntRange(start = startIndex - 1, endInclusive = endIndex + 1)
    val surroundingYRange = IntRange(start = row - 1, endInclusive = row + 1)
}

fun Day03Part1(input: List<String>): Int {
    val components = input.findAllComponents()
    val symbols = components.filterIsInstance<Symbol>()
    val parts = components.filterIsInstance<PartsNumber>()

    fun PartsNumber.hasAdjacentSymbol(symbols: List<Symbol>): Boolean {
        return symbols.any {
            it.row in surroundingYRange && it.column in surroundingXRange
        }
    }

    return parts
        .filter { it.hasAdjacentSymbol(symbols) }
        .sumOf { it.number }
}

fun Day03Part2(input: List<String>): Int {
    fun Symbol.adjacentPartNumbers(components: List<Component>): List<PartsNumber> {
        return components.filterIsInstance<PartsNumber>()
            .filter { partsNumber ->
                this.row in partsNumber.surroundingYRange && this.column in partsNumber.surroundingXRange
            }
    }

    val components = input
        .findAllComponents()

    return components
        .asSequence()
        .filterIsInstance<Symbol>()
        .map { it.adjacentPartNumbers(components) }
        .filter { adjacent -> adjacent.size == 2 }
        .map { it[0].number * it[1].number }
        .sum()
}

fun main() {
    val input = input(day = 3)

    Day03Part1(input.sample()) shouldBe 4361
    Day03Part1(input.main()) shouldBe 549908

    Day03Part2(input.sample()) shouldBe 467835
    Day03Part2(input.main()) shouldBe 81166799
}