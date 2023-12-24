package mmxxiii.util


data class Point(val x: Int, val y: Int)
enum class Direction {
    Up, Down, Left, Right;

    val xOffset
        get() = when (this) {
            Left -> -1
            Right -> 1
            else -> 0
        }

    val yOffset
        get() = when (this) {
            Up -> -1
            Down -> 1
            else -> 0
        }

    val horizontal get() = this == Left || this == Right
    val vertical get() = this == Up || this == Down
}

fun Point.step(direction: Direction, stepSize: Int = 1): Point = copy(
    x = x + (stepSize * direction.xOffset),
    y = y + (stepSize * direction.yOffset)
)

fun Point.neighbours(diagonallyAdjacent: Boolean = true): List<Point> {
    val (x, y) = this

    val extras = if (diagonallyAdjacent) {
        listOf(
            Point(x - 1, y - 1),
            Point(x + 1, y + 1),
            Point(x + 1, y - 1),
            Point(x - 1, y + 1),
        )
    } else {
        emptyList()
    }

    return listOf(
        Point(x - 1, y),
        Point(x + 1, y),
        Point(x, y - 1),
        Point(x, y + 1),
    ) + extras
}

fun List<String>.toPoints(): Map<Point, Char> {
    return flatMapIndexed { y, line ->
        line.mapIndexed { x, c -> Point(x, y) to c }
    }.toMap()
}