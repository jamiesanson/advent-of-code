package mmxxi.helpers

typealias Point = Pair<Int, Int>

fun Point.neighbours(diagonallyAdjacent: Boolean = false): List<Point> {
    val (x, y) = this

    val extras = if (diagonallyAdjacent) {
        listOf(
            x - 1 to y - 1,
            x + 1 to y + 1,
            x + 1 to y - 1,
            x - 1 to y + 1
        )
    } else {
        emptyList()
    }

    return listOf(
        x - 1 to y,
        x + 1 to y,
        x to y - 1,
        x to y + 1
    ) + extras
}