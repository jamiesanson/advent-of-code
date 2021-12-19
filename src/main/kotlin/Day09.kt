import helpers.Point
import helpers.neighbours

fun Day9Part1(input: List<String>): Int {
    val heightMap = input.map { row -> row.trim().toCharArray().map { it.digitToInt() } }

    val lowPoints = mutableListOf<Int>()

    for (x in 0 until heightMap.first().size) {
        for (y in heightMap.indices) {
            val point = heightMap[y][x]

            val left = runCatching { heightMap[y][x - 1] }.getOrElse { Integer.MAX_VALUE }
            val right = runCatching { heightMap[y][x + 1] }.getOrElse { Integer.MAX_VALUE }
            val up = runCatching { heightMap[y - 1][x] }.getOrElse { Integer.MAX_VALUE }
            val down = runCatching { heightMap[y + 1][x] }.getOrElse { Integer.MAX_VALUE }

            if (point < left && point < right && point < up && point < down) {
                lowPoints.add(point)
            }
        }
    }

    return lowPoints.sumOf { it + 1 }
}

fun Day9Part2(input: List<String>): Int {
    val heightMap = input.map { row -> row.trim().toCharArray().map { it.digitToInt() } }

    val lowPoints = mutableListOf<Point>()

    for (x in 0 until heightMap.first().size) {
        for (y in heightMap.indices) {
            val point = heightMap[y][x]

            val left = runCatching { heightMap[y][x - 1] }.getOrElse { Integer.MAX_VALUE }
            val right = runCatching { heightMap[y][x + 1] }.getOrElse { Integer.MAX_VALUE }
            val up = runCatching { heightMap[y - 1][x] }.getOrElse { Integer.MAX_VALUE }
            val down = runCatching { heightMap[y + 1][x] }.getOrElse { Integer.MAX_VALUE }

            if (point < left && point < right && point < up && point < down) {
                lowPoints.add(x to y)
            }
        }
    }

    fun Point.isValid(): Boolean {
        val (x, y) = this
        return x in heightMap.first().indices && y in heightMap.indices
    }

    fun findBasinSize(lowPoint: Point): Int {
        val visitedPoints = mutableSetOf<Point>()
        val pointsToVisit = mutableListOf(lowPoint)

        while (pointsToVisit.isNotEmpty()) {
            val point = pointsToVisit.removeFirst()

            val adjacents = point.neighbours()
                .filter { it.isValid() }
                .filter { it !in visitedPoints }
                .filter { (x, y) -> heightMap[y][x] < 9 }

            visitedPoints.addAll(adjacents)
            pointsToVisit.addAll(adjacents)
        }

        return visitedPoints.size
    }

    return lowPoints
        .map { findBasinSize(it) }
        .sortedByDescending { it }
        .take(3)
        .reduce { acc, size ->  acc * size }
}