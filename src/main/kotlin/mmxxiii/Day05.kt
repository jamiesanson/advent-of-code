package mmxxiii

import io.kotest.matchers.shouldBe
import mmxxiii.input.input
import util.Input

data class DomainMap(
    val ranges: Map<LongRange, LongRange>
) {
    fun convert(source: Long): Long {
        ranges.forEach { (sourceRange, destRange) ->
            if (source in sourceRange) {
                val offset = source - sourceRange.first
                return destRange.first + offset
            }
        }

        return source
    }
}

data class Almanac(
    val seeds: List<Long>,
    val domainMaps: List<DomainMap>,
) {
    fun reversed(): Almanac {
        return copy(
            domainMaps = domainMaps
                .reversed()
                .map {
                    DomainMap(it.ranges.toList().associate { (key, value) -> value to key })
                }
        )
    }

    fun convert(source: Long): Long = domainMaps.fold(source) { domainNumber, map ->
        map.convert(domainNumber)
    }
}


fun List<String>.parseDomainMap(): DomainMap {
    return DomainMap(
        ranges = associate { line ->
            val (destStart, sourceStart, rangeSize) = line
                .split(' ')
                .map { it.trim().toLong() }

            sourceStart..(sourceStart + rangeSize) to destStart..(destStart + rangeSize)
        }
    )
}

fun List<String>.readAlmanac(): Almanac {
    val seeds = first()
        .substringAfter(':')
        .split(' ')
        .filter { it.isNotBlank() }
        .map { it.trim().toLong() }

    val domainMaps = buildList {
        var currentMap = mutableListOf<String>()
        this@readAlmanac
            .drop(2)
            .forEach { line ->
                when {
                    line.isBlank() -> {
                        add(currentMap)
                        currentMap = mutableListOf()
                    }

                    line.first().isDigit() -> {
                        currentMap.add(line)
                    }
                }
            }
    }.map { it.parseDomainMap() }


    return Almanac(
        seeds = seeds,
        domainMaps = domainMaps,
    )
}

fun Day05Part1(input: List<String>): Long {
    val almanac = input.readAlmanac()

    val locationNumbers = almanac.seeds.map { seed ->
        almanac.convert(seed)
    }

    return locationNumbers.min()
}

fun Day05Part2(input: List<String>): Long {
    // Instead of passing seed values through the almanac, reverse mappings and work our way up through
    // location numbers
    val almanac = input.readAlmanac().reversed()

    val seedRanges = almanac.seeds.chunked(size = 2)
        .map { (start, size) -> start..start + size }

    var currentLocationNumber = 0L

    while (true) {
        val seed = almanac.convert(currentLocationNumber)

        if (seedRanges.any { seed in it }) {
            return currentLocationNumber
        } else {
            currentLocationNumber++
        }
    }
}

fun main() {
    val input = input(day = 5)

    Day05Part1(input.sample()) shouldBe 35
    Day05Part1(input.main()) shouldBe 84470622


    Day05Part2(input.sample()) shouldBe 46
    Day05Part2(input.main()) shouldBe 26714516
}