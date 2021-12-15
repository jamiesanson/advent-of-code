package day1

fun Day1(input: String): String {
    return input
        .split('\n')
        .windowed(2)
        .map { (prev, current) -> if (current.toInt() > prev.toInt()) 1 else 0 }
        .sum().toString()
}

fun Day1Part2(input: String): String {
    return input
        .split('\n')
        .windowed(3)
        .map { (one, two, three) -> one.toInt() + two.toInt() + three.toInt() }
        .windowed(2)
        .map { (prev, current) -> if (current > prev) 1 else 0 }
        .sum().toString()
}
