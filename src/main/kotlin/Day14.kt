import java.util.*
import kotlin.collections.HashMap

fun Day14Part1(input: List<String>): Int {
    val template = input.first()

    val rules = input
        .drop(2)
        .map { it.split(" -> ") }
        .associate { (predicate, result) -> predicate to result }

    val steps = 10

    var polymer = template

    repeat(steps) {
        polymer = polymer
            .windowed(2, 1)
            .joinToString("") { chars ->
                val rule = rules[chars]
                chars
                    .withIndex()
                    .joinToString("") { (index, char) ->
                        if (index == 1 && rule != null) "$rule" else "$char"
                    }
            } + polymer.last()
    }


    val grouped = polymer.groupBy { it }
    return grouped.maxOf { it.value.size } - grouped.minOf { it.value.size }
}

@OptIn(ExperimentalStdlibApi::class)
fun Day14Part2(input: List<String>): Long {
    val template = input.first()

    val rules = HashMap(input
        .drop(2)
        .map { it.split(" -> ") }
        .associate { (predicate, result) -> predicate to result })

    val steps = 40

    var polymer = template.windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }

    repeat(steps) {
        val start = System.currentTimeMillis()

        polymer = buildMap {
            polymer.forEach { (pair, count) ->
                val insert = rules[pair]
                if (insert != null) {
                    val firstNew = "${pair[0]}$insert"
                    val secondNew = "$insert${pair[1]}"
                    this[firstNew] = (this[firstNew] ?: 0) + count
                    this[secondNew] = (this[secondNew] ?: 0) + count
                }
            }
        }
        val end = System.currentTimeMillis()
        println("Step $it took ${end - start} millis")
    }


    val grouped = polymer
        .map { it.key.first() to it.value }
        .groupBy({ it.first }, { it.second })
        .mapValues { it.value.sum() + if (it.key == template.last()) 1 else 0 }

    return grouped.values.sorted().let { it.last() - it.first() }
}