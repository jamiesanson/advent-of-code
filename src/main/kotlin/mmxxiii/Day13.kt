package mmxxiii

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import mmxxiii.input.input
import util.Input

private fun List<String>.parsePatterns(): List<Pattern> {
    var emptyLineCount = 0
    return groupBy { line ->
        if (line.isEmpty()) {
            emptyLineCount++
        }

        emptyLineCount
    }.values
        .map { it.filter { line -> line.isNotEmpty() } }
        .map { it.map { line -> line.toList() } }
}

private typealias Pattern = List<List<Char>>

private sealed interface Reflection {
    val index: Int

    data class Horizontal(override val index: Int) : Reflection
    data class Vertical(override val index: Int) : Reflection
}

private fun List<Char>.reflectedIntersectionAround(index: Int): Int {
    if (index == 0) return size

    val left = subList(0, index)
    val right = subList(index, size)

    fun intersect(l: List<Char>, r: List<Char>): Int {
        return l.takeLast(r.size)
            .zip(r.reversed()) { a, b -> if (a != b) 1 else 0 }.sum()
    }

    return if (left.size > right.size) {
        intersect(left, right)
    } else {
        intersect(right.reversed(), left.reversed())
    }
}

private fun findReflection(pattern: Pattern, lenience: Int): Reflection {
    fun Pattern.palindromeIndex(): Int? = first().indices.firstOrNull { idx ->
        sumOf { it.reflectedIntersectionAround(idx) } == lenience
    }

    pattern.palindromeIndex()?.let { return Reflection.Horizontal(it) }

    pattern.rotate().palindromeIndex()?.let { return Reflection.Vertical(it) }

    throw IllegalArgumentException("No reflection found")
}

fun Day13(input: List<String>, lenience: Int = 0): Long = runBlocking {
    val patterns = input.parsePatterns()

    patterns
        .map { async { findReflection(it, lenience) } }
        .awaitAll()
        .sumOf {
            it.index * when (it) {
                is Reflection.Horizontal -> 1L
                is Reflection.Vertical -> 100L
            }
        }
}

fun main() {
    val input = input(day = 13)

    Day13(input.sample()) shouldBe 405
    Day13(input.main()) shouldBe 34911

    Day13(input.sample(), lenience = 1) shouldBe 400
    Day13(input.main(), lenience = 1) shouldBe 33183
}