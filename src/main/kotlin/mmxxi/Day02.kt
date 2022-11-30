package mmxxi

@file:Suppress("KotlinConstantConditions")
fun Day2Part1(input: List<String>): Int {
    var horizontal = 0
    var vertical = 0

    for (line in input) {
        val (dir, count) = line.split(" ")
        when (dir) {
            "forward" -> horizontal += count.toInt()
            "up" -> vertical -= count.toInt()
            "down" -> vertical += count.toInt()
        }
    }

    return horizontal * vertical
}


fun Day2Part2(input: List<String>): Int {
    var horizontal = 0
    var vertical = 0
    var aim = 0

    for (line in input) {
        val (dir, count) = line.split(" ")
        when (dir) {
            "forward" -> {
                horizontal += count.toInt()
                vertical += (aim * count.toInt())
            }
            "up" -> aim -= count.toInt()
            "down" -> aim += count.toInt()
        }
    }

    return horizontal * vertical
}

