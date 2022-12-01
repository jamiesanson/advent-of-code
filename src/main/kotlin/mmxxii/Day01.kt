package mmxxii

private fun getCaloriesPerElf(input: List<String>): List<Int> {
    val calories = MutableList(size = input.filter { it.isBlank() }.size + 1) { 0 }
    var currentIndex = 0

    input.forEach { line ->
        if (line.isBlank()) {
            currentIndex++
            return@forEach
        }

        (calories.getOrElse(currentIndex) { 0 } + line.toInt()).also { calories[currentIndex] = it }
    }

    return calories
}

fun Day01Part1(input: List<String>): Int {
    val calories = getCaloriesPerElf(input)

    return calories.max()
}


fun Day01Part2(input: List<String>): Int {
    val calories = getCaloriesPerElf(input)

    return calories
        .sortedDescending()
        .take(3)
        .sum()
}