package mmxxiii

import io.kotest.matchers.shouldBe
import mmxxiii.input.Input


fun Day01Part1(input: List<String>): Int {
    fun String.calibrationValue(): Int {
        val digits = mapNotNull { it.digitToIntOrNull() }

        return (digits.first() * 10) + digits.last()
    }

    return input.sumOf { line -> line.calibrationValue() }
}

fun Day01Part2(input: List<String>): Int {
    fun String.coerceToDigits(): List<Int> {
        val numbers = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9,
        )

        fun readDigit(fromIndex: Int): Int? {
            return (3..5).firstNotNullOfOrNull { wordLength ->
                val potentialNumber = substring(fromIndex, (fromIndex + wordLength).coerceAtMost(length))
                numbers[potentialNumber]
            }
        }

        return mapIndexedNotNull { index, char ->
            char.digitToIntOrNull() ?: readDigit(fromIndex = index)
        }
    }

    fun String.calibrationValue(): Int {
        val digits = coerceToDigits()

        return (digits.first() * 10) + digits.last()
    }

    return input.sumOf { line -> line.calibrationValue() }
}

fun main() {
    val input = Input(day = "01")

    Day01Part1(input.sample(part = 1)) shouldBe 142
    Day01Part1(input.main()) shouldBe 55607

    Day01Part2(input.sample(part = 2)) shouldBe 281
    Day01Part2(input.main()) shouldBe 55291
}

