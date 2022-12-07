package mmxxii

private fun List<String>.firstIndexOfMarker(markerLength: Int): Int {
    first()
        .windowed(size = markerLength, step = 1)
        .forEachIndexed { index, s ->
            if (s.toSet().size == markerLength) {
                return index + markerLength
            }
        }

    throw IllegalStateException()
}

fun Day06Part1(input: List<String>): Int {
    return input.firstIndexOfMarker(markerLength = 4)
}


fun Day06Part2(input: List<String>): Int {
    return input.firstIndexOfMarker(markerLength = 14)
}