package mmxxiv

import io.kotest.matchers.shouldBe
import mmxxiv.input.input

private fun String.asFileSystem(): MutableList<Long?> {
    val input = toList().map { it.digitToInt() }

    val filesystem = mutableListOf<Long?>()

    for ((index, file) in input.windowed(2, step = 2, partialWindows = true).withIndex()) {
        repeat(file[0]) { filesystem.add(index.toLong()) }

        file.getOrNull(1)?.let { repeat(it) { filesystem.add(null)} }
    }

    return filesystem
}

private fun <T> List<T>.rangeOf(entry: List<T>): IntRange? {
    withIndex()
        .forEach { (index, _) ->
            if (subList(index, (index + entry.size).coerceAtMost(lastIndex)) == entry) {
                return index until index + entry.size
            }
        }

    return null
}

private fun Day9Part1(lines: List<String>): Long {
    val filesystem = lines.first().asFileSystem()

    // Sort the list
    filesystem
        .withIndex()
        .reversed()
        .mapNotNull { (index, value) -> if (value != null) index to value else null }
        .forEach { (index, value) ->
            val leftMostSlot = filesystem.indexOf(null)
            if (leftMostSlot > index) return@forEach

            filesystem[leftMostSlot] = value
            filesystem[index] = null
        }

    return filesystem
        .filterNotNull()
        .withIndex()
        .sumOf { (index, value) -> index * value }
}

private fun Day9Part2(lines: List<String>): Long {
    val filesystem = lines.first().asFileSystem()

    val ranges = filesystem
        .filterNotNull()
        .distinct()
        .associateWith { filesystem.indexOf(it)..filesystem.lastIndexOf(it) }

    ranges
        .entries
        .reversed()
        .forEach { (id, range) ->
            val nulls = range.toList().map { null }
            val slot = filesystem.rangeOf(nulls)

            if (slot != null && slot.first < range.first) {
                for (index in range) {
                    filesystem[index] = null
                }

                for (index in slot) {
                    filesystem[index] = id
                }
            }
        }

    return filesystem
        .withIndex()
        .sumOf { (index, value) -> index * (value ?: 0L) }
}

fun main() {
    val input = input(day = 9)

    Day9Part1(input.sample()) shouldBe 1928
    Day9Part1(input.main()) shouldBe 6463499258318L

    Day9Part2(input.sample()) shouldBe 2858
    Day9Part2(input.main()) shouldBe 6493634986625L
}