import helpers.Point

fun Day13Part1(input: List<String>): Int {
    val points = input
        .takeWhile { it.isNotEmpty() }
        .map { it.trim().split(",") }
        .map { (x, y) -> x.toInt() to y.toInt() }

    // Inflect points around first instruction
    val (axis, value) = input
        .takeLastWhile { it.isNotEmpty() }
        .map { it.split(" ").last().split("=") }
        .map { (axis, value) -> axis to value.toInt() }
        .first()

    val foldHorizontally = axis == "x"

    val newPoints = mutableSetOf<Point>()

    if (foldHorizontally) {
        points.filterTo(newPoints) { (x, _) -> x < value }

        points
            .filter { (x, _) -> x > value }
            .mapTo(newPoints) { (x, y) -> (value - (x - value)) to y }
    } else {
        points.filterTo(newPoints) { (_, y) -> y < value }

        points
            .filter { (_, y) -> y > value }
            .mapTo(newPoints) { (x, y) -> x to (value - (y - value)) }
    }

    return newPoints.size
}


fun Day13Part2(input: List<String>): Int {
    val points = input
        .takeWhile { it.isNotEmpty() }
        .map { it.trim().split(",") }
        .map { (x, y) -> x.toInt() to y.toInt() }
        .toMutableList()

    val instructions = input
        .takeLastWhile { it.isNotEmpty() }
        .map { it.split(" ").last().split("=") }
        .map { (axis, value) -> axis to value.toInt() }

    for ((axis, value) in instructions) {
        val foldHorizontally = axis == "x"

        val newPoints = mutableSetOf<Point>()

        if (foldHorizontally) {
            points.filterTo(newPoints) { (x, _) -> x < value }

            points
                .filter { (x, _) -> x > value }
                .mapTo(newPoints) { (x, y) -> (value - (x - value)) to y }
        } else {
            points.filterTo(newPoints) { (_, y) -> y < value }

            points
                .filter { (_, y) -> y > value }
                .mapTo(newPoints) { (x, y) -> x to (value - (y - value)) }
        }

        points.clear()
        points.addAll(newPoints)
    }

    val xMax = points.maxOf { it.first }
    val yMax = points.maxOf { it.second }

    for (y in 0..yMax) {
        for (x in 0..xMax) {
            val char = if (points.any { it.first == x && it.second == y }) "#" else "."
            print(char)
        }
        println()
    }

    return points.size
}