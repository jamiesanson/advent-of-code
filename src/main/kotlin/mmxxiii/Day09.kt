package mmxxiii

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.*
import mmxxiii.input.Input

private fun generateDivisors(original: List<Int>): List<List<Int>> {
    val divisors = mutableListOf<List<Int>>()

    while (divisors.lastOrNull()?.all { it == 0 } != true) {
        val previous = divisors.lastOrNull() ?: original
        val next = buildList {
            previous.windowed(2) { (first, second) ->
                add(second - first)
            }
        }

        divisors.add(next)
    }

    // Put this at the start of the divisor list to allow the fold to use the last value
    divisors.add(index = 0, original)

    return divisors.toList()
}

fun List<Int>.foldDivisors(operation: (List<Int>, Int) -> Int): Int {
    val divisors = generateDivisors(this)

    return divisors.foldRight(0, operation)
}

fun List<Int>.predictNextElement(): Int =
    foldDivisors { list, current -> current + list.last() }

fun List<Int>.predictPreviousElement(): Int =
    foldDivisors { list, current -> list.first() - current }

fun Day09Part1(input: List<String>): Int {
    val sequences = input.map { line -> line.split(' ').map { it.toInt() } }

    val predictions = runBlocking {
        sequences
            .map { sequence ->
                async { sequence.predictNextElement() }
            }
            .awaitAll()
    }

    return predictions.sum()
}

fun Day09Part2(input: List<String>): Int {
    val sequences = input.map { line -> line.split(' ').map { it.toInt() } }

    val predictions = runBlocking {
        sequences
            .map { sequence ->
                async { sequence.predictPreviousElement() }
            }
            .awaitAll()
    }

    return predictions.sum()
}

fun main() {
    val input = Input("09")

    Day09Part1(input.sample()) shouldBe 114
    Day09Part1(input.main()) shouldBe 2043677056

    Day09Part2(input.sample()) shouldBe 2
    Day09Part2(input.main()) shouldBe 1062
}