package mmxxiii

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import mmxxiii.input.Input

private const val Broken = '#'
private const val Operational = '.'

data class Arrangement(
    val springs: String, val layout: List<Int>
)

private fun String.unfold(): String {
    val (springs, layout) = split(" ")
    return List(5) { springs }.joinToString("?") + " " + List(5) { layout }.joinToString(",")
}

private fun String.toArrangement(): Arrangement {
    val line = split(" ")
    val springs = line[0]
    val layout = line[1].split(',').map { it.toInt() }

    return Arrangement(springs, layout)
}

private fun String.broken(): Int = count { it == Broken }
private fun String.operational(): Int = count { it == Operational }

private fun Arrangement.countPossibleCombinations(): Long {
    val memo = mutableMapOf<Arrangement, Long>()
    return DeepRecursiveFunction<Arrangement, Long> { (springs, layout) ->
        val trimmed = springs.trim(Operational)
        memo.getOrPut(Arrangement(trimmed, layout)) {
            val layoutSum = layout.sum()
            when {
                trimmed.broken() > layoutSum ||
                        layoutSum > trimmed.length - trimmed.operational() ||
                        layoutSum + layout.size - 1 > trimmed.length
                -> 0

                trimmed.isEmpty() || layout.isEmpty() -> 1
                else -> {
                    if (
                        trimmed.subSequence(1, layout[0]).all { it != Operational } &&
                        trimmed.getOrNull(layout[0]) != Broken
                    ) {
                        callRecursive(
                            value = Arrangement(
                                springs = trimmed.drop(layout[0] + 1),
                                layout = layout.subList(1, layout.size)
                            )
                        )
                    } else {
                        0
                    } + if (!trimmed.startsWith(Broken)) {
                        callRecursive(
                            value = Arrangement(
                                springs = trimmed.drop(1),
                                layout = layout
                            )
                        )
                    } else {
                        0
                    }
                }
            }
        }
    }(this)
}

fun Day12Part1(input: List<String>): Long {
    return runBlocking {
        input
            .map { async { it.toArrangement().countPossibleCombinations() } }
            .awaitAll()
            .sum()
    }
}


fun Day12Part2(input: List<String>): Long {
    return runBlocking {
        input
            .map { async { it.unfold().toArrangement().countPossibleCombinations() } }
            .awaitAll()
            .sum()
    }
}

fun main() {
    val input = Input("12")

    Day12Part1(input.sample()) shouldBe 21
    Day12Part1(input.main()) shouldBe 7705

    Day12Part2(input.sample()) shouldBe 525152
    Day12Part2(input.main()) shouldBe 50338344809230L
}