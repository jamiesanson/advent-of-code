package mmxxiii

import io.kotest.matchers.shouldBe
import mmxxiii.input.Input

data class Race(
    val time: Long,
    val recordDistance: Long,
) {
    fun timings(): List<Long> = List(time.toInt()) { elapsedTime ->
        elapsedTime * (time - elapsedTime)
    }
}

private fun List<Race>.combine(block: (Race) -> Long): Long {
    return joinToString(separator = "") { block(it).toString() }.toLong()
}

private fun List<String>.parseRaceRecords(): List<Race> {
    fun String.extractInts(): List<Long> = dropWhile { !it.isDigit() }
        .split(' ')
        .filter { it.isNotBlank() }
        .map { it.trim().toLong() }

    val times = first().extractInts()
    val distances = get(1).extractInts()

    return times.zip(distances).map { (time, distance) -> Race(time, distance) }
}

fun Day06Part1(input: List<String>): Int {
    val races = input.parseRaceRecords()

    val marginsOfError = races.map { race ->
        race.timings().count { it > race.recordDistance }
    }

    return marginsOfError.reduce { acc, i -> acc * i }
}

fun Day06Part2(input: List<String>): Int {
    val races = input.parseRaceRecords()

    val race = Race(
        time = races.combine { it.time },
        recordDistance = races.combine { it.recordDistance },
    )

    return race.timings().count { it > race.recordDistance }
}

fun main() {
    val input = Input("06")

    Day06Part1(input.sample()) shouldBe 288
    Day06Part1(input.main()) shouldBe 3317888

    Day06Part2(input.sample()) shouldBe 71503
    Day06Part2(input.main()) shouldBe 24655068
}