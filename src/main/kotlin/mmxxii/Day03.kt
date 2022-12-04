package mmxxii


fun Day03Part1(input: List<String>): Int {
    val sacks = input.map {
        val first = it.take(it.length / 2)
        val second = it.takeLast(it.length / 2)

        first.toSet() to second.toSet()
    }

    return sacks.fold(0) { acc, (first, second) ->
        val intersection = first.intersect(second).first()
        val charValue = intersection.uppercase().first().code - 64
        acc + (charValue + if (intersection.isUpperCase()) 26 else 0)
    }
}


fun Day03Part2(input: List<String>): Int {
    val sacks = input.map { it.toSet() }

    return sacks
        .windowed(size = 3, step = 3)
        .sumOf { (one, two, three) ->
            val intersection = one.intersect(two).intersect(three).first()
            (intersection.uppercase().first().code - 64) + if (intersection.isUpperCase()) 26 else 0
        }
}