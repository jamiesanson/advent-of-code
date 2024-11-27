package mmxxiii

import io.kotest.matchers.shouldBe
import mmxxiii.input.input
import util.Input

data class Connector(
    val character: Char,
    val location: Pair<Int, Int>,
) {
    val isAnimal = character == 'S'

    private val north by lazy { character in setOf('L', 'J', '|', 'S') }
    private val south by lazy { character in setOf('7', 'F', '|', 'S') }
    private val west by lazy { character in setOf('7', 'J', '-', 'S') }
    private val east by lazy { character in setOf('L', 'F', '-', 'S') }

    val x by lazy { location.first }
    val y by lazy { location.second }

    enum class Direction {
        North, South, East, West
    }
    fun directionTo(connector: Connector): Direction? {
        return when {
            // No diagonal connection
            x != connector.x && y != connector.y ->
                null

            x == connector.x + 1 && connector.east && west ->
                Direction.East

            x == connector.x - 1 && connector.west && east ->
                Direction.West

            connector.y == y + 1 && connector.north && south ->
                Direction.South

            connector.y == y - 1 && connector.south && north ->
                Direction.North

            else -> null
        }
    }

    fun windingAngle(previous: Connector): Int {
        val directionFromPrevious = previous.directionTo(this)
        return when (character) {
            '-' -> 0
            '|' -> if (directionFromPrevious == Direction.North) 2 else -2
            'F' -> if (directionFromPrevious == Direction.North) 1 else -1
            'L' -> if (directionFromPrevious == Direction.East) 1 else -1
            '7' -> if (directionFromPrevious == Direction.West) -1 else 1
            'J' -> if (directionFromPrevious == Direction.South) -1 else 1
            else -> 0
        }
    }
}

// Figure out what the S character should be given previous and next ones
fun Connector.coerceToCharacter(previous: Connector, next: Connector): Connector {
    val directionTo = previous.directionTo(this)
    val directionFrom = this.directionTo(next)

    return copy(
        character = when (directionTo) {
            Connector.Direction.North -> {
                when (directionFrom) {
                    Connector.Direction.North -> '|'
                    Connector.Direction.East -> 'F'
                    Connector.Direction.West -> '7'
                    else -> error("")
                }
            }
            Connector.Direction.South -> {
                when (directionFrom) {
                    Connector.Direction.South -> '|'
                    Connector.Direction.East -> 'L'
                    Connector.Direction.West -> 'J'
                    else -> error("")
                }
            }
            Connector.Direction.East -> {
                when (directionFrom) {
                    Connector.Direction.East -> '-'
                    Connector.Direction.South -> '7'
                    Connector.Direction.North -> 'J'
                    else -> error("")
                }
            }
            Connector.Direction.West -> {
                when (directionFrom) {
                    Connector.Direction.West -> '-'
                    Connector.Direction.South -> 'F'
                    Connector.Direction.North -> 'L'
                    else -> error("")
                }
            }
            else -> error("")
        }
    )
}

fun List<String>.parsePipes(): List<List<Connector>> {
    return mapIndexed { y, line ->
        line.mapIndexed { x, char ->
            Connector(
                character = char,
                location = x to y
            )
        }
    }
}

val List<List<Connector>>.startingLocation: Pair<Int, Int>
    get() {
        forEachIndexed { y, connectors ->
            connectors.forEachIndexed { x, connector ->
                if (connector.isAnimal) return x to y
            }
        }

        error("No animal found")
    }

fun <T> List<List<T>>.surrounding(x: Int, y: Int): List<T> {
    val surroundingPoints = listOf(
        x - 1 to y - 1,
        x to y - 1,
        x + 1 to y - 1,
        x + 1 to y,
        x to y + 1,
        x - 1 to y + 1,
        x - 1 to y,
    )

    return surroundingPoints
        .filter { (x, y) -> x in this[0].indices && y in indices }
        .map { (x, y) -> this[y][x] }
}

fun List<Connector>.calculateWindings(): List<Int> {
    val windings = windowed(size = 2, step = 1) { (previous, current) ->
        current.windingAngle(previous)
    }

    val firstCharacter = first().coerceToCharacter(previous = last(), next = get(1))

    return listOf(firstCharacter.windingAngle(previous = last())) + windings
}

fun List<List<Connector>>.adjoining(connector: Connector): List<Connector> {
    val (x, y) = connector.location

    return surrounding(x, y)
        .mapNotNull { candidate ->
            val direction = connector.directionTo(candidate)
            if (direction == null) null else candidate
        }
}

fun connectLoop(connectors: List<List<Connector>>, start: Pair<Int, Int>): List<Connector> {
    val loop = mutableListOf(connectors[start.second][start.first])

    while (true) {
        val currentLocation = loop.last()

        val next = connectors
            .adjoining(currentLocation)
            .firstOrNull { it !in loop }

        if (next == null) {
            break
        } else {
            loop.add(next)
        }
    }

    return loop
}

fun List<List<Connector>>.print(
    greenConnectors: List<Connector>,
    blueConnectors: List<Connector>,
) {
    val ansiReset = "\u001B[0m"
    val ansiGreen = "\u001B[32m"
    val ansiBlue = "\u001B[34;1m"

    for (row in this) {
        for (connector in row) {
            when (connector) {
                in greenConnectors ->
                    print(ansiGreen + connector.character + ansiReset)

                in blueConnectors ->
                    print(ansiBlue + connector.character + ansiReset)

                else -> print(connector.character)
            }
        }
        println()
    }
}

fun Day10Part1(input: List<String>): Int {
    val connectors = input.parsePipes()

    val loop = connectLoop(
        connectors = connectors,
        start = connectors.startingLocation
    )

    return loop.size / 2
}

fun Day10Part2(input: List<String>, debug: Boolean = false): Int {
    val connectors = input.parsePipes()

    val loop = connectLoop(
        connectors = connectors,
        start = connectors.startingLocation
    )

    val windings = loop.calculateWindings()

    windings.size shouldBe loop.size

    val loopPositionsWithWindingOffset = loop
        .map { it.location }
        .zip(windings)
        .toMap()

    val inside = connectors
        .flatMap { row ->
            row.mapNotNull { connector ->
                // Don't check points in loop
                if (connector in loop) {
                    return@mapNotNull null
                }

                // If it's on the edge, it's not in the loop
                if (connector.x == 0 || connector.y == 0 || connector.x == connectors[0].lastIndex || connector.y == connectors.lastIndex) {
                    return@mapNotNull null
                }

                // If there's nothing to the left of this point, it's not in the loop
                var hasLoopOnLeft = false
                for (x in connector.x downTo 0) {
                    hasLoopOnLeft = loopPositionsWithWindingOffset[x to connector.y] != null || hasLoopOnLeft
                }

                if (!hasLoopOnLeft) {
                    return@mapNotNull null
                }

                // Scan left to right, sum winding angle. If the angle is 0, we're not in the loop
                var windingAngle = 0
                for (x in connector.x until row.size) {
                    loopPositionsWithWindingOffset[x to connector.y]?.let { windingAngle += it }
                }

                if (windingAngle == 0) null else connector
            }
        }

    if (debug) {
        connectors.print(
            greenConnectors = loop,
            blueConnectors = inside,
        )
    }

    return inside.size
}

fun main() {
    val input = input(day = 10)

    Day10Part1(input.sample()) shouldBe 4
    Day10Part1(input.sample(part = 1)) shouldBe 8
    Day10Part1(input.main()) shouldBe 6882

    Day10Part2(input.sample(part = 2)) shouldBe 4
    Day10Part2(input.sample(part = 4)) shouldBe 8
    Day10Part2(input.sample(part = 3)) shouldBe 10
    Day10Part2(input.main()) shouldBe 491
}
